package entity;

import javax.persistence.*;

/**
 * Kitap entity sinifi.
 * Kutuphane sistemindeki kitaplari temsil eder.
 */
@Entity
@Table(name = "kitaplar")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "baslik", nullable = false)
    private String baslik;

    @Column(name = "yazar", nullable = false)
    private String yazar;

    @Column(name = "yayin_yili")
    private int yayinYili;

    @Column(name = "durum", nullable = false)
    private String durum;

    // Kitap ile Odunc arasinda OneToOne iliski (Book -> Loan)
    @OneToOne(mappedBy = "kitap", fetch = FetchType.LAZY)
    private Loan odunc;

    // Kitap durumu sabitleri
    public static final String MUSAIT = "MUSAIT";
    public static final String ODUNC_VERILDI = "ODUNC_VERILDI";

    // Varsayilan constructor (Hibernate icin gerekli)
    public Book() {
    }

    // Parametreli constructor
    public Book(String baslik, String yazar, int yayinYili) {
        this.baslik = baslik;
        this.yazar = yazar;
        this.yayinYili = yayinYili;
        this.durum = MUSAIT;
    }

    // Getter ve Setter metotlari
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getYazar() {
        return yazar;
    }

    public void setYazar(String yazar) {
        this.yazar = yazar;
    }

    public int getYayinYili() {
        return yayinYili;
    }

    public void setYayinYili(int yayinYili) {
        this.yayinYili = yayinYili;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public Loan getOdunc() {
        return odunc;
    }

    public void setOdunc(Loan odunc) {
        this.odunc = odunc;
    }

    @Override
    public String toString() {
        return "Kitap [ID=" + id + ", Baslik=" + baslik + ", Yazar=" + yazar +
                ", Yayin Yili=" + yayinYili + ", Durum=" + durum + "]";
    }
}
