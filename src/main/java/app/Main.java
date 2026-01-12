package app;

import dao.BookDao;
import dao.LoanDao;
import dao.StudentDao;
import entity.Book;
import entity.Loan;
import entity.Student;
import util.HibernateUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * SmartLibraryPlus Ana Uygulama Sinifi.
 * Konsol tabanli kutuphane yonetim sistemi.
 */
public class Main {

    private static final Scanner tarayici = new Scanner(System.in);
    private static final BookDao kitapDao = new BookDao();
    private static final StudentDao ogrenciDao = new StudentDao();
    private static final LoanDao oduncDao = new LoanDao();
    private static final DateTimeFormatter tarihFormati = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        // Hibernate loglarini kapat
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.OFF);

        System.out.println("========================================");
        System.out.println("  SmartLibrary2- Akilli Kutuphane  ");
        System.out.println("========================================");
        System.out.println();

        boolean devamEt = true;

        while (devamEt) {
            menuGoster();
            int secim = sayiOku("Seciminiz: ");

            switch (secim) {
                case 1:
                    kitapEkle();
                    break;
                case 2:
                    kitaplariListele();
                    break;
                case 3:
                    ogrenciEkle();
                    break;
                case 4:
                    ogrencileriListele();
                    break;
                case 5:
                    kitapOduncVer();
                    break;
                case 6:
                    oduncListesiniGoruntule();
                    break;
                case 7:
                    kitapGeriTeslimAl();
                    break;
                case 0:
                    devamEt = false;
                    System.out.println("Programdan cikiliyor...");
                    break;
                default:
                    System.out.println("Gecersiz secim! Lutfen tekrar deneyin.");
            }
            System.out.println();
        }

        // Uygulama kapanirken Hibernate'i kapat
        HibernateUtil.kapat();
        tarayici.close();
    }

    /**
     * Ana menuyu ekrana yazdirir.
     */
    private static void menuGoster() {
        System.out.println("----------------------------------------");
        System.out.println("             ANA MENU                   ");
        System.out.println("----------------------------------------");
        System.out.println("1 - Kitap Ekle");
        System.out.println("2 - Kitaplari Listele");
        System.out.println("3 - Ogrenci Ekle");
        System.out.println("4 - Ogrencileri Listele");
        System.out.println("5 - Kitap Odunc Ver");
        System.out.println("6 - Odunc Listesini Goruntule");
        System.out.println("7 - Kitap Geri Teslim Al");
        System.out.println("0 - Cikis");
        System.out.println("----------------------------------------");
    }

    /**
     * Yeni kitap ekleme islemi.
     */
    private static void kitapEkle() {
        System.out.println("\n--- KITAP EKLEME ---");

        System.out.print("Kitap Basligi: ");
        String baslik = tarayici.nextLine();

        System.out.print("Yazar: ");
        String yazar = tarayici.nextLine();

        int yayinYili = sayiOku("Yayin Yili: ");

        Book yeniKitap = new Book(baslik, yazar, yayinYili);
        kitapDao.kaydet(yeniKitap);
    }

    /**
     * Tum kitaplari listeler.
     */
    private static void kitaplariListele() {
        System.out.println("\n--- KITAP LISTESI ---");

        List<Book> kitaplar = kitapDao.tumunuGetir();

        if (kitaplar == null || kitaplar.isEmpty()) {
            System.out.println("Sistemde kayitli kitap bulunmamaktadir.");
            return;
        }

        System.out.println("-----------------------------------------------------------------------");
        System.out.printf("%-5s | %-25s | %-20s | %-6s | %-15s%n",
                "ID", "Baslik", "Yazar", "Yil", "Durum");
        System.out.println("-----------------------------------------------------------------------");

        for (Book kitap : kitaplar) {
            String durumMetni = kitap.getDurum().equals(Book.MUSAIT) ? "Musait" : "Odunc Verildi";
            System.out.printf("%-5d | %-25s | %-20s | %-6d | %-15s%n",
                    kitap.getId(),
                    kisalt(kitap.getBaslik(), 25),
                    kisalt(kitap.getYazar(), 20),
                    kitap.getYayinYili(),
                    durumMetni);
        }
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Toplam " + kitaplar.size() + " kitap listelendi.");
    }

    /**
     * Yeni ogrenci ekleme islemi.
     */
    private static void ogrenciEkle() {
        System.out.println("\n--- OGRENCI EKLEME ---");

        System.out.print("Ogrenci Adi: ");
        String ad = tarayici.nextLine();

        System.out.print("Bolum: ");
        String bolum = tarayici.nextLine();

        Student yeniOgrenci = new Student(ad, bolum);
        ogrenciDao.kaydet(yeniOgrenci);
    }

    /**
     * Tum ogrencileri listeler.
     */
    private static void ogrencileriListele() {
        System.out.println("\n--- OGRENCI LISTESI ---");

        List<Student> ogrenciler = ogrenciDao.tumunuGetir();

        if (ogrenciler == null || ogrenciler.isEmpty()) {
            System.out.println("Sistemde kayitli ogrenci bulunmamaktadir.");
            return;
        }

        System.out.println("---------------------------------------------");
        System.out.printf("%-5s | %-25s | %-20s%n", "ID", "Ad", "Bolum");
        System.out.println("---------------------------------------------");

        for (Student ogrenci : ogrenciler) {
            System.out.printf("%-5d | %-25s | %-20s%n",
                    ogrenci.getId(),
                    kisalt(ogrenci.getAd(), 25),
                    kisalt(ogrenci.getBolum(), 20));
        }
        System.out.println("---------------------------------------------");
        System.out.println("Toplam " + ogrenciler.size() + " ogrenci listelendi.");
    }

    /**
     * Kitap odunc verme islemi.
     */
    private static void kitapOduncVer() {
        System.out.println("\n--- KITAP ODUNC VERME ---");

        // Mevcut ogrencileri goster
        ogrencileriListele();
        Long ogrenciId = uzunSayiOku("Ogrenci ID: ");

        Student ogrenci = ogrenciDao.idIleBul(ogrenciId);
        if (ogrenci == null) {
            System.out.println("Hata: Belirtilen ID'ye sahip ogrenci bulunamadi!");
            return;
        }

        // Mevcut kitaplari goster
        kitaplariListele();
        Long kitapId = uzunSayiOku("Kitap ID: ");

        Book kitap = kitapDao.idIleBul(kitapId);
        if (kitap == null) {
            System.out.println("Hata: Belirtilen ID'ye sahip kitap bulunamadi!");
            return;
        }

        // Kitabin durumunu kontrol et
        if (kitap.getDurum().equals(Book.ODUNC_VERILDI)) {
            System.out.println("Hata: Bu kitap zaten odunc verilmis durumda!");
            System.out.println("Kitap: " + kitap.getBaslik());
            return;
        }

        // Odunc tarihini al
        LocalDate alisTarihi = tarihOku("Odunc Alma Tarihi (GG/AA/YYYY): ");
        if (alisTarihi == null) {
            alisTarihi = LocalDate.now();
            System.out.println("Tarih olarak bugunun tarihi kullanildi: " + alisTarihi.format(tarihFormati));
        }

        try {
            // Odunc kaydini olustur
            Loan yeniOdunc = new Loan(ogrenci, kitap, alisTarihi);
            oduncDao.kaydet(yeniOdunc);

            // Kitabin durumunu guncelle
            kitap.setDurum(Book.ODUNC_VERILDI);
            kitapDao.guncelle(kitap);

            System.out.println("Kitap basariyla odunc verildi.");
            System.out.println("Ogrenci: " + ogrenci.getAd());
            System.out.println("Kitap: " + kitap.getBaslik());
            System.out.println("Odunc Tarihi: " + alisTarihi.format(tarihFormati));
        } catch (Exception e) {
            System.out.println("Hata: Odunc islemi gerceklestirilemedi!");
            System.out.println("Bu kitap icin zaten bir odunc kaydi olabilir.");
        }
    }

    /**
     * Tum odunc kayitlarini listeler.
     */
    private static void oduncListesiniGoruntule() {
        System.out.println("\n--- ODUNC LISTESI ---");

        List<Loan> oduncler = oduncDao.tumunuGetir();

        if (oduncler == null || oduncler.isEmpty()) {
            System.out.println("Sistemde odunc kaydi bulunmamaktadir.");
            return;
        }

        System.out.println(
                "--------------------------------------------------------------------------------------------");
        System.out.printf("%-5s | %-20s | %-25s | %-12s | %-12s%n",
                "ID", "Ogrenci", "Kitap", "Alis Tarihi", "Iade Tarihi");
        System.out.println(
                "--------------------------------------------------------------------------------------------");

        for (Loan odunc : oduncler) {
            String iadeTarihiStr = (odunc.getIadeTarihi() != null)
                    ? odunc.getIadeTarihi().format(tarihFormati)
                    : "Iade edilmedi";

            System.out.printf("%-5d | %-20s | %-25s | %-12s | %-12s%n",
                    odunc.getId(),
                    kisalt(odunc.getOgrenci().getAd(), 20),
                    kisalt(odunc.getKitap().getBaslik(), 25),
                    odunc.getAlisTarihi().format(tarihFormati),
                    iadeTarihiStr);
        }
        System.out.println(
                "--------------------------------------------------------------------------------------------");
        System.out.println("Toplam " + oduncler.size() + " odunc kaydi listelendi.");
    }

    /**
     * Kitap geri teslim alma islemi.
     */
    private static void kitapGeriTeslimAl() {
        System.out.println("\n--- KITAP GERI TESLIM ALMA ---");

        // Aktif odunc kayitlarini goster
        oduncListesiniGoruntule();

        Long oduncId = uzunSayiOku("Teslim alinacak odunc kayit ID: ");

        Loan odunc = oduncDao.idIleBul(oduncId);
        if (odunc == null) {
            System.out.println("Hata: Belirtilen ID'ye sahip odunc kaydi bulunamadi!");
            return;
        }

        if (odunc.getIadeTarihi() != null) {
            System.out.println("Hata: Bu kitap zaten iade edilmis!");
            System.out.println("Iade Tarihi: " + odunc.getIadeTarihi().format(tarihFormati));
            return;
        }

        // Iade tarihini al
        LocalDate iadeTarihi = tarihOku("Iade Tarihi (GG/AA/YYYY): ");
        if (iadeTarihi == null) {
            iadeTarihi = LocalDate.now();
            System.out.println("Tarih olarak bugunun tarihi kullanildi: " + iadeTarihi.format(tarihFormati));
        }

        // Odunc kaydini guncelle
        odunc.setIadeTarihi(iadeTarihi);
        oduncDao.guncelle(odunc);

        // Kitabin durumunu guncelle
        Book kitap = odunc.getKitap();
        kitap.setDurum(Book.MUSAIT);
        kitapDao.guncelle(kitap);

        System.out.println("Kitap basariyla teslim alindi.");
        System.out.println("Kitap: " + kitap.getBaslik());
        System.out.println("Iade Tarihi: " + iadeTarihi.format(tarihFormati));
    }

    /**
     * Kullanicidan tam sayi okur.
     * 
     * @param mesaj Kullaniciya gosterilecek mesaj
     * @return Okunan tam sayi
     */
    private static int sayiOku(String mesaj) {
        while (true) {
            try {
                System.out.print(mesaj);
                int sayi = Integer.parseInt(tarayici.nextLine().trim());
                return sayi;
            } catch (NumberFormatException e) {
                System.out.println("Gecersiz sayi! Lutfen tekrar deneyin.");
            }
        }
    }

    /**
     * Kullanicidan uzun tam sayi (Long) okur.
     * 
     * @param mesaj Kullaniciya gosterilecek mesaj
     * @return Okunan Long sayi
     */
    private static Long uzunSayiOku(String mesaj) {
        while (true) {
            try {
                System.out.print(mesaj);
                Long sayi = Long.parseLong(tarayici.nextLine().trim());
                return sayi;
            } catch (NumberFormatException e) {
                System.out.println("Gecersiz sayi! Lutfen tekrar deneyin.");
            }
        }
    }

    /**
     * Kullanicidan tarih okur.
     * 
     * @param mesaj Kullaniciya gosterilecek mesaj
     * @return Okunan tarih veya bos girilirse null
     */
    private static LocalDate tarihOku(String mesaj) {
        System.out.print(mesaj);
        String girdi = tarayici.nextLine().trim();

        if (girdi.isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(girdi, tarihFormati);
        } catch (DateTimeParseException e) {
            System.out.println("Gecersiz tarih formati! Bugunun tarihi kullanilacak.");
            return null;
        }
    }

    /**
     * Metni belirtilen uzunlukta kisaltir.
     * 
     * @param metin      Kisaltilacak metin
     * @param maxUzunluk Maksimum uzunluk
     * @return Kisaltilmis metin
     */
    private static String kisalt(String metin, int maxUzunluk) {
        if (metin == null) {
            return "";
        }
        if (metin.length() <= maxUzunluk) {
            return metin;
        }
        return metin.substring(0, maxUzunluk - 3) + "...";
    }
}
