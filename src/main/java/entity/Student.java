package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Ogrenci entity sinifi.
 * Kutuphane sistemindeki ogrencileri temsil eder.
 */
@Entity
@Table(name = "ogrenciler")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ad", nullable = false)
    private String ad;

    @Column(name = "bolum", nullable = false)
    private String bolum;

    // Bir ogrencinin birden fazla odunc alma kaydi olabilir
    @OneToMany(mappedBy = "ogrenci", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Loan> oduncListesi = new ArrayList<>();

    // Varsayilan constructor (Hibernate icin gerekli)
    public Student() {
    }

    // Parametreli constructor
    public Student(String ad, String bolum) {
        this.ad = ad;
        this.bolum = bolum;
    }

    // Getter ve Setter metotlari
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getBolum() {
        return bolum;
    }

    public void setBolum(String bolum) {
        this.bolum = bolum;
    }

    public List<Loan> getOduncListesi() {
        return oduncListesi;
    }

    public void setOduncListesi(List<Loan> oduncListesi) {
        this.oduncListesi = oduncListesi;
    }

    // Odunc ekleme yardimci metodu
    public void oduncEkle(Loan odunc) {
        oduncListesi.add(odunc);
        odunc.setOgrenci(this);
    }

    @Override
    public String toString() {
        return "Ogrenci [ID=" + id + ", Ad=" + ad + ", Bolum=" + bolum + "]";
    }
}
