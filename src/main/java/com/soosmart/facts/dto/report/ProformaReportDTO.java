package com.soosmart.facts.dto.report;

import java.util.Date;

public class ProformaReportDTO {
    String reference;
    String numero;
    Date date;
    String client;
    String lieu;
    String role;
    String signby;
    String lettresomme;
    Double total_ht;
    Double tva;
    Double total_ttc;

    public ProformaReportDTO() {
        super();
    }

    public ProformaReportDTO(String reference, String numero, Date date, String client, String lieu, String role, String signby, String lettresomme, Double total_ht, Double tva, Double total_ttc) {
        super();
        this.reference = reference;
        this.numero = numero;
        this.date = date;
        this.client = client;
        this.lieu = lieu;
        this.role = role;
        this.signby = signby;
        this.lettresomme = lettresomme;
        this.total_ht = total_ht;
        this.tva = tva;
        this.total_ttc = total_ttc;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSignby() {
        return signby;
    }

    public void setSignby(String signby) {
        this.signby = signby;
    }

    public String getLettresomme() {
        return lettresomme;
    }

    public void setLettresomme(String lettresomme) {
        this.lettresomme = lettresomme;
    }

    public Double getTotal_ht() {
        return total_ht;
    }

    public void setTotal_ht(Double total_ht) {
        this.total_ht = total_ht;
    }

    public Double getTva() {
        return tva;
    }

    public void setTva(Double tva) {
        this.tva = tva;
    }

    public Double getTotal_ttc() {
        return total_ttc;
    }

    public void setTotal_ttc(Double total_ttc) {
        this.total_ttc = total_ttc;
    }
}
