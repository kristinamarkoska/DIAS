package com.example.project1.data.pipeline.impl;

import com.example.project1.data.DataTransformer;
import com.example.project1.data.pipeline.Filter;
import com.example.project1.entity.AssetHistory;
import com.example.project1.entity.CompanyProfile;
import com.example.project1.repository.CompanyProfileRepository;
import com.example.project1.repository.AssetHistoryRepository;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class FilterThree implements Filter<List<CompanyProfile>> {

    private final CompanyProfileRepository companyProfileRepository;
    private final AssetHistoryRepository assetHistoryRepository;

    private static final String HISTORICAL_DATA_URL = "https://www.mse.mk/mk/stats/symbolhistory/";

    public FilterThree(CompanyProfileRepository companyProfileRepository, AssetHistoryRepository assetHistoryRepository) {
        this.companyProfileRepository = companyProfileRepository;
        this.assetHistoryRepository = assetHistoryRepository;
    }

    public List<CompanyProfile> execute(List<CompanyProfile> input) throws IOException, ParseException {

        for (CompanyProfile companyProfile : input) {
            LocalDate fromDate = LocalDate.now();
            LocalDate toDate = LocalDate.now().plusYears(1);
            addHistoricalData(companyProfile, fromDate, toDate);
        }

        return null;
    }

    private void addHistoricalData(CompanyProfile companyProfile, LocalDate fromDate, LocalDate toDate) throws IOException, ParseException {
        Connection.Response response = Jsoup.connect(HISTORICAL_DATA_URL + companyProfile.getCompanyCode())
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
                    LocalDate date = DataTransformer.parseDate(columns.get(0).text(), "d.M.yyyy");

                    if (date != null && assetHistoryRepository.findByDateAndCompany(date, companyProfile).isEmpty()) {
                        if (companyProfile.getLastUpdated() == null || companyProfile.getLastUpdated().isBefore(date)) {
                            companyProfile.setLastUpdated(date);
                            companyProfileRepository.save(companyProfile);
                        }

                        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);

                        Double lastTransactionPrice = DataTransformer.parseDouble(columns.get(1).text(), format);
                        Double maxPrice = DataTransformer.parseDouble(columns.get(2).text(), format);
                        Double minPrice = DataTransformer.parseDouble(columns.get(3).text(), format);
                        Double averagePrice = DataTransformer.parseDouble(columns.get(4).text(), format);
                        Double percentageChange = DataTransformer.parseDouble(columns.get(5).text(), format);
                        Integer quantity = DataTransformer.parseInteger(columns.get(6).text(), format);
                        Integer turnoverBest = DataTransformer.parseInteger(columns.get(7).text(), format);
                        Integer totalTurnover = DataTransformer.parseInteger(columns.get(8).text(), format);

                        AssetHistory assetHistory = new AssetHistory(
                                date, lastTransactionPrice, maxPrice, minPrice, averagePrice, percentageChange,
                                quantity, turnoverBest, totalTurnover);
                        assetHistory.setCompanyProfile(companyProfile);

                        assetHistoryRepository.save(assetHistory);
                    }
                }
            }
        }
    }


}
