package com.eastday.schedule;

import com.eastday.dao.Princeandcity;
import com.eastday.dao.PrinceandcityRepository;
import com.eastday.dto.RetDto;
import com.eastday.service.PrinceandcityService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: zengzhewen
 * @create: 2020-04-17 10:56
 **/
@Component
@Slf4j
public class SchedulePublish {

    @Autowired
    PrinceandcityService princeandcityService;

    @Autowired
    PrinceandcityRepository princeandcityRepository;

    @Scheduled(cron = "0 30 06 * * ?")
    public void updateWeather(){
        String ct = null;
        try {
            //1.获取所有城市信息列表
            List<Princeandcity> allPrinceandcity = princeandcityService.findAllPrinceandcity();

            //2.根据城市获取天气数据
            for(Princeandcity princeandcity:allPrinceandcity){

                String city = princeandcity.getCity();
                ct=city;

                JSONObject dataOfJson = princeandcityService.findRetWeatherDtoByCity(city);

                //3.将天气数据转成需要的格式数据
                Princeandcity newprinceandcity = princeandcityService.retWeatherDtoToPrinceandcity(dataOfJson);

                if(newprinceandcity == null){
                    log.info("接口获取天气数据失败,city:"+ct);
                    princeandcityRepository.editPrinceandcityStatus(princeandcity.getId(),0);
                    continue;
                }

                //4.天气数据更新到数据库
                princeandcityRepository.editPrinceandcityByCity(princeandcity.getId(),newprinceandcity.getMsg(),newprinceandcity.getUpdateTime());

            }
        }catch (Exception e){
            log.info("未知异常,city:"+ct);
            log.error(e.toString());
        }
        log.info("\n更新完成...");
    }
}
