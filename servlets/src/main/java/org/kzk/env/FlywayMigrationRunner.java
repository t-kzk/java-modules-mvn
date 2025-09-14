package org.kzk.env;

import org.flywaydb.core.Flyway;

public class FlywayMigrationRunner {
    public static void main(String[] args) {
        // Настройка Flyway
        Flyway flyway = Flyway.configure()
                .dataSource(DataSourceProviderPG.INSTANCE.getDataSource())
                .defaultSchema("module_2_4_servlet")
               // .locations("classpath:db/migration")
                .load();

        // Запуск миграций
        flyway.migrate();
    }
}
