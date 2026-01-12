package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate SessionFactory yonetim sinifi.
 * Singleton tasarim deseni kullanilmistir.
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    // Statik blok ile SessionFactory olusturma
    static {
        try {
            // hibernate.cfg.xml dosyasindan konfigurasyonu yukle
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

            sessionFactory = configuration.buildSessionFactory();
            System.out.println("Hibernate SessionFactory basariyla olusturuldu.");
        } catch (Exception e) {
            System.err.println("SessionFactory olusturulurken hata olustu: " + e.getMessage());
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * SessionFactory nesnesini dondurur.
     * 
     * @return SessionFactory nesnesi
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * SessionFactory'yi kapatir.
     * Uygulama sonlandiginda cagrilmalidir.
     */
    public static void kapat() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("Hibernate SessionFactory kapatildi.");
        }
    }
}
