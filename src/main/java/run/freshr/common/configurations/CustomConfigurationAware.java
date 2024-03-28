package run.freshr.common.configurations;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * 필수 추가 설정 항목 설정
 *
 * @author 류성재
 * @apiNote 필수 추가 설정 항목 설정
 * @since 2024. 3. 28. 오후 1:29:05
 */
@Data
@Configuration
@EnableConfigurationProperties
public class CustomConfigurationAware {

  /**
   * Health Check 화면 경로
   *
   * @apiNote Health Check 화면 경로<br>
   *          화면은 필요가 없음<br>
   *          개인의 욕심
   * @since 2024. 3. 28. 오후 1:29:05
   */
  @Value("classpath:static/heartbeat/index.htm")
  private Resource heartbeat;

}
