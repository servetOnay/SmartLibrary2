# SmartLibrary2 - Kütüphane Yönetim Sistemi

## Giriş

Merhaba, bu projeyi Nesneye Yönelik Programlama dersim için hazırlayarak Hibernate ORM konusunu pratikte uygulama fırsatı buldum. Aslında basit bir kütüphane otomasyonu gibi görünse de arka planda epey uğraştıran detaylar vardı, özellikle entity ilişkileri ve lazy loading konuları beni bir hayli zorladığı için bu yazıda o süreci de anlatmak istedim.

Projenin temel amacı JDBC ile doğrudan SQL yazmak yerine Hibernate kullanarak veritabanı işlemlerini yönetmeyi öğrenmekti. Böylece Java nesneleri üzerinden çalışmak mümkün hale geldi ve kod çok daha okunaklı oldu bence.

## Neden Bu Teknolojileri Seçtim

Aslında başlangıçta MySQL düşünmüştüm ama SQLite ile çalışmak daha pratik geldi çünkü kurulum gerektirmiyor, tek bir dosya olarak çalışıyor. Ödev için de yeterli olduğunu düşündüğümden tercihim bu yönde oldu.

Hibernate konusunda ise doğrusu zorlandım ilk başta. Annotation mantığı, session yönetimi, transaction açıp kapatma gibi konular kafa karıştırıcı gelebiliyor ama bir süre uğraşınca oturuyor kafada.

Kullandığım teknolojiler:
- Java 17
- Hibernate ORM 5.6.15
- SQLite veritabanı
- Maven (bağımlılıkları yönetmek için)

## Proje Klasör Yapısı

Projeyi katmanlı bir mimaride organize etmeye çalıştım:

```
SmartLibrary2/
|-- src/main/java/
|   |-- entity/      -> Veritabanı tablolarını temsil eden sınıflar
|   |-- dao/         -> Veritabanı işlemlerini yapan sınıflar
|   |-- util/        -> Yardımcı sınıflar (HibernateUtil)
|   |-- app/         -> Ana uygulama
|-- src/main/resources/
|   |-- hibernate.cfg.xml
|-- pom.xml
|-- README.md
```

## Entity Sınıfları ve İlişkiler

Bu kısım beni en çok uğraştıran bölüm oldu açık konuşmak gerekirse. Üç tane entity sınıfı oluşturdum:

**Book**: Kitap bilgilerini tutuyor. Başlık, yazar, yayın yılı ve müsait mi ödünç verilmiş mi bilgisi var.

**Student**: Öğrenci bilgileri için. Ad ve bölüm bilgisi tutuyorum.

**Loan**: Ödünç alma işlemlerini kaydettim buraya. Hangi öğrenci hangi kitabı ne zaman almış, ne zaman iade etmiş gibi bilgiler.

İlişkiler konusunda şu annotationları kullandım:
- Student ile Loan arasında OneToMany var, yani bir öğrenci birden fazla kitap ödünç alabilir
- Loan tarafında ManyToOne ile öğrenciye bağlantı kurdum
- Loan ile Book arasında da OneToOne ilişki tanımladım çünkü bir kitap aynı anda sadece bir kişi tarafından ödünç alınabiliyor

Bu ilişkileri kurarken lazy loading yüzünden LazyInitializationException hatası aldım sürekli. Session kapandıktan sonra ilişkili nesnelere erişilmeye çalışılınca bu hata çıkıyor. Çözüm olarak JOIN FETCH kullanmak zorunda kaldım sorgularda.

## DAO Katmanı

Her entity için ayrı bir DAO sınıfı yazdım. İçlerinde şu metodlar var:
- kaydet: Yeni kayıt ekliyor
- guncelle: Var olan kaydı güncelliyor
- sil: Kayıt siliyor
- idIleBul: ID'ye göre tek kayıt getiriyor
- tumunuGetir: Tüm kayıtları listeliyor

Transaction yönetimini doğru yapmak önemli, try-catch içinde commit veya rollback yapmak gerekiyor yoksa veritabanı tutarsız kalabiliyor.

## Konsol Arayüzü

Basit bir menü sistemi kurdum:

```
1 - Kitap Ekle
2 - Kitapları Listele
3 - Öğrenci Ekle
4 - Öğrencileri Listele
5 - Kitap Ödünç Ver
6 - Ödünç Listesini Görüntüle
7 - Kitap Geri Teslim Al
0 - Çıkış
```

Her seçenekte kullanıcıdan gerekli bilgileri alıp ilgili DAO metodlarını çağırıyorum. Mesela kitap ödünç verirken önce kitabın müsait olup olmadığını kontrol ediyorum, eğer zaten ödünç verilmişse kullanıcıyı uyarıyorum.

## Kurulum

Projeyi çalıştırmak için:

1. Java 17 ve Maven kurulu olmalı
2. Proje dizinine gidip `mvn clean compile` komutu ile derleme
3. Ardından `mvn exec:java` ile çalıştırma

SQLite veritabanı dosyası otomatik oluşturuluyor, ayrı bir kurulum gerekmiyor.

## Yaşadığım Sorunlar ve Çözümler

Projeyi yaparken birkaç sorunla karşılaştım, belki başkalarının da işine yarar diye yazayım:

1. **LazyInitializationException**: Session kapandıktan sonra lazy yüklemeli alanlara erişmeye çalışmak bu hataya yol açıyor. JOIN FETCH ile sorguyu yazınca çözüldü.

2. **Transaction rollback hatası**: Bazen transaction zaten kapanmış olduğunda rollback yapmaya çalışınca hata alıyordum. transaction.isActive() kontrolü ekleyerek çözdüm.

3. **UNIQUE constraint hatası**: Aynı kitap için birden fazla ödünç kaydı oluşturmaya çalışınca SQLite hata verdi. Zaten ödünç verilmiş kitabı tekrar ödünç vermeye çalışmamak için kontrol ekledim.

## Sonuç

Bu proje sayesinde Hibernate ORM konusunda pratik kazandım. Özellikle entity ilişkileri ve session yönetimi konularında zorlandığım kısımlar oldu ama sonunda çalışır hale getirdiğimde güzel bir öğrenme deneyimi yaşadım. İleride daha büyük projelerde bu bilgileri kullanabileceğimi düşünüyorum.
