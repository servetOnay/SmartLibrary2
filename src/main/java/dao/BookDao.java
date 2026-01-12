package dao;

import entity.Book;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

/**
 * Kitap entity'si icin Data Access Object (DAO) sinifi.
 * Kitap ile ilgili tum veritabani islemlerini yonetir.
 */
public class BookDao {

    /**
     * Yeni bir kitap kaydeder.
     * 
     * @param kitap Kaydedilecek kitap nesnesi
     */
    public void kaydet(Book kitap) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(kitap);
            transaction.commit();
            System.out.println("Kitap basariyla kaydedildi: " + kitap.getBaslik());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Kitap kaydedilirken hata olustu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Mevcut bir kitabi gunceller.
     * 
     * @param kitap Guncellenecek kitap nesnesi
     */
    public void guncelle(Book kitap) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(kitap);
            transaction.commit();
            System.out.println("Kitap basariyla guncellendi: " + kitap.getBaslik());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Kitap guncellenirken hata olustu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Bir kitabi ID'ye gore siler.
     * 
     * @param id Silinecek kitabin ID'si
     */
    public void sil(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Book kitap = session.get(Book.class, id);
            if (kitap != null) {
                session.delete(kitap);
                transaction.commit();
                System.out.println("Kitap basariyla silindi: " + kitap.getBaslik());
            } else {
                System.out.println("Silinecek kitap bulunamadi. ID: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Kitap silinirken hata olustu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Bir kitabi ID'ye gore getirir.
     * 
     * @param id Aranan kitabin ID'si
     * @return Bulunan kitap nesnesi veya null
     */
    public Book idIleBul(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Book.class, id);
        } catch (Exception e) {
            System.err.println("Kitap aranirken hata olustu: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Tum kitaplari getirir.
     * 
     * @return Kitap listesi
     */
    public List<Book> tumunuGetir() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Book", Book.class).list();
        } catch (Exception e) {
            System.err.println("Kitaplar listelenirken hata olustu: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
