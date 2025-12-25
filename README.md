# API Test Automation

Bu proje, REST API test otomasyonu için hazırlanmış bir test projesidir. WireMock kullanılarak mock API'lar oluşturulmuş ve bu API'lar üzerinde çeşitli test senaryoları gerçekleştirilmiştir.

## Geliştirici

**Burak Doğan** - [@burakdogan27](https://github.com/burakdogan27)

## Kurulum

```bash
git clone https://github.com/burakdogan27/api-test-automation.git
cd api-test-automation
mvn spring-javaformat:apply
mvn clean install -DskipTests
```

## Test Çalıştırma

```bash
mvn clean test
```

## Teknolojiler

- Java 17
- JUnit 5
- RestAssured
- WireMock
- Allure
- Maven
