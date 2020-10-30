package com.eastday.controller;

import com.eastday.aop.Timer;
import com.eastday.dao.Princeandcity;
import com.eastday.dao.PrinceandcityRepository;
import com.eastday.dto.CityBo;
import com.eastday.dto.JsonData;
import com.eastday.dto.RetWeatherDto;
import com.eastday.service.PrinceandcityService;
import com.eastday.utils.JsonResult;
import com.eastday.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author: zengzhewen
 * @create: 2020-04-20 10:58
 **/

@Slf4j
@RestController
@RequestMapping(value = "/weather")
public class PrinceandcityController {

    @Autowired
    PrinceandcityService princeandcityService;

    @Autowired
    PrinceandcityRepository princeandcityRepository;

    @Value("${specicalcity}")
    private List specicalcity;
    /**
     * 更新所有城市天气
     * @return
     */
    @Timer
    @GetMapping(value = "/renewAll")
    public boolean updatePrinceandcity(){
        String cityName="";
        try {
            //1.获取所有城市信息列表
            List<Princeandcity> allPrinceandcity = princeandcityService.findAllPrinceandcity();

            //2.根据城市获取天气数据
            for(Princeandcity princeandcity:allPrinceandcity){

                String city = princeandcity.getCity();
                cityName=city;

                JSONObject dataOfJson = princeandcityService.findRetWeatherDtoByCity(city);

                //3.将天气数据转成需要的格式数据
                Princeandcity newprinceandcity = princeandcityService.retWeatherDtoToPrinceandcity(dataOfJson);

                if(newprinceandcity == null){
                    log.info(city);
                    princeandcityRepository.editPrinceandcityStatus(princeandcity.getId(),0);
                    continue;
                    //return new RetDto(false,"weather_mini接口调用异常", null);
                }

                //4.天气数据更新到数据库
                princeandcityRepository.editPrinceandcityByCity(princeandcity.getId(),newprinceandcity.getMsg(),newprinceandcity.getUpdateTime());
            }
            return true;
        }catch (Exception e){
            log.info("未知异常，城市:"+cityName);
            log.error(e.toString());
        }
        return false;
    }

    /**
     * 根据城市名称获取天气数据
     * @return
     */
    @Timer
    @PostMapping(value = "/api/data")
    public JsonResult updatePrinceandcityByCityName(@RequestBody CityBo cityBo){

        if(cityBo == null){
            return JsonResult.ok();
        }
        String city =cityBo.getCity();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        //1.校验入参
        log.info("入参 city："+city);
        if(StringUtils.isBlank(city)){
            log.info("参数有误，city为空");
            return JsonResult.ok();
        }
        try {
            //2.查询城市天气信息
            List<Princeandcity> list = princeandcityRepository.findByCity(city);
            if(list.size() == 0){
                log.info("城市名称获取为空");
                return JsonResult.ok();
            }
            //如有重复城市名，取第1条数据
            Princeandcity princeandcity = list.get(0);

            if(princeandcity.getStatus()==0){
                log.info("无法更新该城市天气，返回旧数据");
                JsonData jsonData = (JsonData) JsonUtils.StringToObject(princeandcity.getMsg(),RetWeatherDto.class);

                JsonResult.ok(jsonData);
            }
            //3.判断请求间隔时间是否有15分钟
            int time=princeandcityService.timeDifference(princeandcity.getUpdateTime());
            log.info("间隔时间："+time);

            //请求时间间隔小于15分钟
            if(time < 60*60 || !specicalcity.contains(city)){
                JsonData jsonData = (JsonData) JsonUtils.StringToObject(princeandcity.getMsg(),JsonData.class);
                return JsonResult.ok(jsonData);
            }else {
                JSONObject dataOfJson = princeandcityService.findRetWeatherDtoByCity(city);

                Princeandcity newprinceandcity = princeandcityService.retWeatherDtoToPrinceandcity(dataOfJson);

                if(newprinceandcity==null){
                    log.info("城市信息获取失败，城市:"+city);
                    return JsonResult.ok();
                }
                princeandcityRepository.editPrinceandcityByCity(princeandcity.getId(),newprinceandcity.getMsg(),newprinceandcity.getUpdateTime());
                //更新对象信息，返回数据

                JsonData jsonData = (JsonData) JsonUtils.StringToObject(newprinceandcity.getMsg(),JsonData.class);

                return JsonResult.ok(jsonData);
            }
        }catch (Exception e){
            log.info("未知异常，城市名称："+city);
            log.error(e.toString());
        }
        return JsonResult.ok();
    }
}
