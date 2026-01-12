package dao;

import entity.Loan;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

/**
 * Odunc entity'si icin Data Access Object (DAO) sinifi.
 * Odunc alma islemleri ile ilgili tum veritabani islemlerini yonetir.
 */
public class LoanDao {

    /**
     * Yeni bir odunc kaydi olusturur.
     * 
     * @param odunc Kaydedilecek odunc nesnesi
     */
    public void kaydet(Loan odunc) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(odunc);
            transaction.commit();
            System.out.println("Odunc kaydi basariyla olusturuldu.");
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackEx) {
                    System.err.println("Rollback sirasinda hata: " + rollbackEx.getMessage());
                }
            }
            System.err.println("Odunc kaydi olusturulurken hata olustu: " + e.getMessage());
            throw new RuntimeException("Odunc kaydi olusturulamadi", e);
        }
    }

    /**
     * Mevcut bir odunc kaydini gunceller.
     * 
     * @param odunc Guncellenecek odunc nesnesi
     */
    public void guncelle(Loan odunc) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(odunc);
            transaction.commit();
            System.out.println("Odunc kaydi basariyla guncellendi.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Odunc kaydi guncellenirken hata olustu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Bir odunc kaydini ID'ye gore siler.
     * 
     * @param id Silinecek odunc kaydinin ID'si
     */
    public void sil(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Loan odunc = session.get(Loan.class, id);
            if (odunc != null) {
                session.delete(odunc);
                transaction.commit();
                System.out.println("Odunc kaydi basariyla silindi.");
            } else {
                System.out.println("Silinecek odunc kaydi bulunamadi. ID: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Odunc kaydi silinirken hata olustu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Bir odunc kaydini ID'ye gore getirir.
     * 
     * @param id Aranan odunc kaydinin ID'si
     * @return Bulunan odunc nesnesi veya null
     */
    public Loan idIleBul(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT l FROM Loan l JOIN FETCH l.ogrenci JOIN FETCH l.kitap WHERE l.id = :id";
            List<Loan> sonuclar = session.createQuery(hql, Loan.class)
                    .setParameter("id", id)
                    .list();
            return sonuclar.isEmpty() ? null : sonuclar.get(0);
        } catch (Exception e) {
            System.err.println("Odunc kaydi aranirken hata olustu: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Tum odunc kayitlarini getirir.
     * 
     * @return Odunc listesi
     */
    public List<Loan> tumunuGetir() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT l FROM Loan l JOIN FETCH l.ogrenci JOIN FETCH l.kitap";
            return session.createQuery(hql, Loan.class).list();
        } catch (Exception e) {
            System.err.println("Odunc kayitlari listelenirken hata olustu: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Belirli bir kitaba ait aktif (iade edilmemis) odunc kaydini bulur.
     * 
     * @param kitapId Aranan kitabin ID'si
     * @return Aktif odunc kaydi veya null
     */
    public Loan kitapIdIleAktifOduncBul(Long kitapId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Loan l WHERE l.kitap.id = :kitapId AND l.iadeTarihi IS NULL";
            List<Loan> sonuclar = session.createQuery(hql, Loan.class)
                    .setParameter("kitapId", kitapId)
                    .list();
            return sonuclar.isEmpty() ? null : sonuclar.get(0);
        } catch (Exception e) {
            System.err.println("Aktif odunc kaydi aranirken hata olustu: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
