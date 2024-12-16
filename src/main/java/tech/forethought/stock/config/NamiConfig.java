package tech.forethought.stock.config;

import org.noear.nami.NamiBuilder;
import org.noear.nami.NamiConfiguration;
import org.noear.nami.annotation.NamiClient;
import org.noear.nami.coder.snack3.SnackDecoder;
import org.noear.nami.coder.snack3.SnackTypeEncoder;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;

@Configuration
public class NamiConfig {
    @Bean
    public NamiConfiguration initNami() {
        return new NamiConfiguration() {
            @Override
            public void config(NamiClient client, NamiBuilder builder) {
                builder.timeout(60 * 2);
                builder.decoder(SnackDecoder.instance);
                builder.encoder(SnackTypeEncoder.instance);
            }
        };
    }
}
