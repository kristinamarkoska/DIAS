package mk.tradesense.tradesense.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Signal_Profil implements Serializable {
    private String stockCode;
    private Date date;
}