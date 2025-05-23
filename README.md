# SUI Indexer
## Индексатрр распределенного реестра SUI

**SUI Indexer** — это высокопроизводительный индексатор блокчейна SUI, предназначенный для извлечения, структурирования и \
хранения ончейн-данных в формате, удобном для последующего анализа и использования в dApps, аналитике и мониторинге.


### Используемые технологии
- Язык программирования: Rust / Java 21 + Spring Boot
- База данных: PostgreSQL / ClickHouse
- Брокер сообщений: Kafka
- Подключение к SUI: SUI Custom Indexer - [https://docs.sui.io/guides/developer/advanced/custom-indexer](https://docs.sui.io/guides/developer/advanced/custom-indexer)
- API: REST


### Установка
```bash
git clone https://github.com/your-org/sui-indexer.git
cd sui-indexer
docker-compose up -d
```

Docker:
```bash
docker-compose up
```

Kubernetes: \
Подробную инструкцию о развертывании кластера kubernetes можно посимотреть здесь - [README.md](./kubernetes/README.md)
