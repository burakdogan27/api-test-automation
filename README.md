# API Otomasyon Projesi

Bu proje, REST API test otomasyonu için hazırlanmış bir test projesidir. WireMock kullanılarak mock API'lar oluşturulmuş ve bu API'lar üzerinde çeşitli test senaryoları gerçekleştirilmiştir.

## Ön Gereksinimler

- Java 17 veya üzeri
- Maven 3.6.3 veya üzeri
- Git

## Kurulum

1. Projeyi klonlayın:
   ```bash
   git clone https://github.com/username/api-automation-case-study.git
   cd api-automation-case-study
   ```

2. Bağımlılıkları yükleyin:
   ```bash
   mvn clean install -DskipTests
   ```

## Testleri Çalıştırma

Tüm testleri çalıştırmak için:
```bash
mvn clean test
```

Allure raporu oluşturmak için:
```bash
mvn allure:serve
```

## Proje Yapısı

```
src/
├── main/java/api/
│   ├── client/         # API istemci sınıfları
│   ├── mock/           # WireMock stub tanımları
│   └── models/         # Request/Response modelleri
│
└── test/java/api/
    ├── BaseTest.java           # Temel test sınıfı
    └── ApiIntegrationTest.java # Test senaryoları
```

## Kullanılan Teknolojiler

- **Test Framework**: JUnit 5
- **API Test**: RestAssured
- **Mock Server**: WireMock
- **Raporlama**: Allure
- **Build Tool**: Maven
- **Utility**: Lombok, Jackson
