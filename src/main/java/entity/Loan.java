package entity;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Odunc alma entity sinifi.
 * Kitap odunc alma islemlerini temsil eder.
 */
@Entity
@Table(name = "odunc_islemleri")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alis_tarihi", nullable = false)
    private LocalDate alisTarihi;

    @Column(name = "iade_tarihi")
    private LocalDate iadeTarihi;

    // Bir odunc islemi bir ogrenciye aittir (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ogrenci_id", nullable = false)
    private Student ogrenci;

    // Bir odunc islemi bir kitaba aittir (OneToOne)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kitap_id", nullable = false)
    private Book kitap;

    // Varsayilan constructor (Hibernate icin gerekli)
    public Loan() {
    }

    // Parametreli constructor
    public Loan(Student ogrenci, Book kitap, LocalDate alisTarihi) {
        this.ogrenci = ogrenci;
        this.kitap = kitap;
        this.alisTarihi = alisTarihi;
        this.iadeTarihi = null;
    }

    // Getter ve Setter metotlari
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAlisTarihi() {
        return alisTarihi;
    }

    public void setAlisTarihi(LocalDate alisTarihi) {
        this.alisTarihi = alisTarihi;
    }

    public LocalDate getIadeTarihi() {
        return iadeTarihi;
    }

    public void setIadeTarihi(LocalDate iadeTarihi) {
        this.iadeTarihi = iadeTarihi;
    }

    public Student getOgrenci() {
        return ogrenci;
    }

    public void setOgrenci(Student ogrenci) {
        this.ogrenci = ogrenci;
    }

    public Book getKitap() {
        return kitap;
    }

    public void setKitap(Book kitap) {
        this.kitap = kitap;
    }

    @Override
    public String toString() {
        String iadeStr = (iadeTarihi != null) ? iadeTarihi.toString() : "Henuz iade edilmedi";
        return "Odunc [ID=" + id + ", Ogrenci=" + ogrenci.getAd() +
                ", Kitap=" + kitap.getBaslik() + ", Alis Tarihi=" + alisTarihi +
                ", Iade Tarihi=" + iadeStr + "]";
    }
}
