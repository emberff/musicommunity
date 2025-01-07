import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(scanBasePackages = {"com.music"})
@ServletComponentScan
@MapperScan({"com.music.common.**.mapper"})
public class MusicommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicommunityApplication.class,args);
    }

}