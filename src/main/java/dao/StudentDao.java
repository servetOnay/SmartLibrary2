package dao;

import entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

/**
 * Ogrenci entity'si icin Data Access Object (DAO) sinifi.
 * Ogrenci ile ilgili tum veritabani islemlerini yonetir.
 */
public class StudentDao {

    /**
     * Yeni bir ogrenci kaydeder.
     * 
     * @param ogrenci Kaydedilecek ogrenci nesnesi
     */
    public void kaydet(Student ogrenci) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(ogrenci);
            transaction.commit();
            System.out.println("Ogrenci basariyla kaydedildi: " + ogrenci.getAd());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Ogrenci kaydedilirken hata olustu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Mevcut bir ogrenciyi gunceller.
     * 
     * @param ogrenci Guncellenecek ogrenci nesnesi
     */
    public void guncelle(Student ogrenci) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(ogrenci);
            transaction.commit();
            System.out.println("Ogrenci basariyla guncellendi: " + ogrenci.getAd());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Ogrenci guncellenirken hata olustu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Bir ogrenciyi ID'ye gore siler.
     * 
     * @param id Silinecek ogrencinin ID'si
     */
    public void sil(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student ogrenci = session.get(Student.class, id);
            if (ogrenci != null) {
                session.delete(ogrenci);
                transaction.commit();
                System.out.println("Ogrenci basariyla silindi: " + ogrenci.getAd());
            } else {
                System.out.println("Silinecek ogrenci bulunamadi. ID: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Ogrenci silinirken hata olustu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Bir ogrenciyi ID'ye gore getirir.
     * 
     * @param id Aranan ogrencinin ID'si
     * @return Bulunan ogrenci nesnesi veya null
     */
    public Student idIleBul(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Student.class, id);
        } catch (Exception e) {
            System.err.println("Ogrenci aranirken hata olustu: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Tum ogrencileri getirir.
     * 
     * @return Ogrenci listesi
     */
    public List<Student> tumunuGetir() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student", Student.class).list();
        } catch (Exception e) {
            System.err.println("Ogrenciler listelenirken hata olustu: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
