package com.example.project1.data.pipeline.impl;

import com.example.project1.data.Parser;
import com.example.project1.data.pipeline.Filter;
import com.example.project1.model.CompanyIssuerModel;
import com.example.project1.model.CompanyDataModel;
import com.example.project1.repository.CompanyDataRepository;
import com.example.project1.repository.CompanyIssuerRepository;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FilterTwo implements Filter<List<CompanyIssuerModel>> {

    private final CompanyIssuerRepository companyIssuerRepository;
    private final CompanyDataRepository companyDataRepository;

    private static final String HISTORICAL_DATA_URL = "https://www.mse.mk/mk/stats/symbolhistory/";

    public FilterTwo(CompanyIssuerRepository companyIssuerRepository, CompanyDataRepository companyDataRepository) {
        this.companyIssuerRepository = companyIssuerRepository;
        this.companyDataRepository = companyDataRepository;
    }

    public List<CompanyIssuerModel> execute(List<CompanyIssuerModel> input) throws IOException {
        List<CompanyIssuerModel> companies = new ArrayList<>();

        for (CompanyIssuerModel company : input) {
            if (company.getLastUpdated() == null) {
                for (int i = 1; i <= 10; i++) {
                    int temp = i - 1;
                    LocalDate fromDate = LocalDate.now().minusYears(i);
                    LocalDate toDate = LocalDate.now().minusYears(temp);
                    addHistoricalData(company, fromDate, toDate);
                }
            } else {
                companies.add(company);
            }
        }

        return companies;
    }

    private void addHistoricalData(CompanyIssuerModel company, LocalDate fromDate, LocalDate toDate) throws IOException {
        Connection.Response response = Jsoup.connect(HISTORICAL_DATA_URL + company.getCompanyCode())
                .data("FromDate", fromDate.toString())
                .data("ToDate", toDate.toString())
                .method(Connection.Method.POST)
                .execute();

        Document document = response.parse();

        Element table = document.select("table#resultsTable").first();

        if (table != null) {
            Elements rows = table.select("tbody tr");

            for (Element row : rows) {
                Elements columns = row.select("td");

                if (columns.size() > 0) {
                    LocalDate date = Parser.parseDate(columns.get(0).text(), "d.M.yyyy");

                    if (companyDataRepository.findByDateAndCompany(date, company).isEmpty()) {


                        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);

                        Double lastTransactionPrice = Parser.parseDouble(columns.get(1).text(), format);
                        Double maxPrice = Parser.parseDouble(columns.get(2).text(), format);
                        Double minPrice = Parser.parseDouble(columns.get(3).text(), format);
                        Double averagePrice = Parser.parseDouble(columns.get(4).text(), format);
                        Double percentageChange = Parser.parseDouble(columns.get(5).text(), format);
                        Integer quantity = Parser.parseInteger(columns.get(6).text(), format);
                        Integer turnoverBest = Parser.parseInteger(columns.get(7).text(), format);
                        Integer totalTurnover = Parser.parseInteger(columns.get(8).text(), format);

                        if (maxPrice != null) {

                            if (company.getLastUpdated() == null || company.getLastUpdated().isBefore(date)) {
                                company.setLastUpdated(date);
                            }

                            CompanyDataModel companyDataModel = new CompanyDataModel(
                                    date, lastTransactionPrice, maxPrice, minPrice, averagePrice, percentageChange,
                                    quantity, turnoverBest, totalTurnover);
                            companyDataModel.setCompany(company);
                            companyDataRepository.save(companyDataModel);
                            company.getHistoricalData().add(companyDataModel);
                        }
                    }
                }
            }
        }

        companyIssuerRepository.save(company);
    }

}
