package org.kzk;

import org.kzk.entity.Writer;
import org.kzk.repository.impl.WriterRepositoryImpl;

public class DemoApp {

    public static void main(String[] args) {
        WriterRepositoryImpl wr = new WriterRepositoryImpl();

        Writer tanya = Writer.builder()
                .username("Tanya")
                .build();
        wr.save(tanya);
    }
}
