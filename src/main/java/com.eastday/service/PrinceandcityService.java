package com.eastday.service;

import com.eastday.dao.Princeandcity;
import com.eastday.dao.PrinceandcityRepository;
import com.eastday.dto.RetWeatherDto;
import com.eastday.dto.WeacherCityInfo;
import com.eastday.dto.WeatherData;
import com.eastday.utils.HttpClientUtil;
import com.eastday.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author: zengzhewen
 * @create: 2020-04-17 11:08
 **/
@Slf4j
@Service
public class PrinceandcityService {

    @Autowired
    PrinceandcityRepository princeandcityRepository;

    //请求天气URL
    @Value("${weather_mini_url}")
    private String weather_mini_url;

    private String dayPictureUrl="http://api.map.baidu.com/images/weather/day/";
    private String nightPictureUrl="http://api.map.baidu.com/images/weather/night/";

    //获取城市信息(status=1)
    public List<Princeandcity> findAllPrinceandcity(){

        List<Princeandcity> princeandcitys = princeandcityRepository.findAllByStatus(1);
        return princeandcitys;
    }

    /**
     * 根据城市名称，获取天气数据
     * @param city
     * @return
     */
    public JSONObject findRetWeatherDtoByCity(String city){

        HashMap<String, String> map = new HashMap<>();
        map.put("city",city);

        String json = HttpClientUtil.doGet(weather_mini_url, map);
        JSONObject dataOfJson = JSONObject.fromObject(json);
        return dataOfJson;
    }

    /**
     * 将获取天气数据转化为所需格式数据
     * @param dataOfJson
     * @return
     */
    public Princeandcity retWeatherDtoToPrinceandcity(JSONObject dataOfJson){
        if(dataOfJson.getInt("status")!=1000){
            log.info("weather_mini接口调用异常");
            return null;
        }
        //从json数据中提取数据
        String data = dataOfJson.getString("data");
        dataOfJson = JSONObject.fromObject(data);
        String city = dataOfJson.getString("city");//城市
        //获取天气预报信息列表
        JSONArray forecast = dataOfJson.getJSONArray("forecast");
        //实时温度
        String wendu = dataOfJson.getString("wendu");

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        SimpleDateFormat df2 = new SimpleDateFormat("MM月dd日");

        //当天数据
        WeatherData weatherData1 = new WeatherData();
        getTodayWeather(weatherData1,forecast.getJSONObject(0),df2,wendu);

        //第2天数据
        WeatherData weatherData2 = new WeatherData();
        getNextWeather(weatherData2,forecast.getJSONObject(1));

        //第3天数据
        WeatherData weatherData3 = new WeatherData();
        getNextWeather(weatherData3,forecast.getJSONObject(2));

        //第4天数据
        WeatherData weatherData4 = new WeatherData();
        getNextWeather(weatherData4,forecast.getJSONObject(3));

        List<WeatherData> list = new ArrayList<>();
        list.add(weatherData1);
        list.add(weatherData2);
        list.add(weatherData3);
        list.add(weatherData4);

        WeacherCityInfo weacherCityInfo = new WeacherCityInfo();
        weacherCityInfo.setCurrentCity(city);
        weacherCityInfo.setPm25("");
        weacherCityInfo.setWeather_data(list);

        List list2 = new ArrayList<>();
        list2.add(weacherCityInfo);

        RetWeatherDto retWeatherDto = new RetWeatherDto();
        retWeatherDto.setError(0);
        retWeatherDto.setStatus("success");
        retWeatherDto.setDate(df.format(new Date()));
        retWeatherDto.setResults(list2);
        String string = com.alibaba.fastjson.JSON.toJSONString(retWeatherDto);

        //返回对象
        Princeandcity princeandcity = new Princeandcity();
        princeandcity.setMsg(string);
        long timeStampSec = System.currentTimeMillis()/1000;
        String timestamp = String.format("%010d", timeStampSec);
        princeandcity.setUpdateTime(Integer.parseInt(timestamp));

        return princeandcity;
    }

    //获取当天天气数据
    public void getTodayWeather(WeatherData weatherData,JSONObject jsonObject,SimpleDateFormat df2,String wendu){
        weatherData.setDate(formatDate(jsonObject.getString("date"))+" "+df2.format(new Date())+" (实时："+wendu+"℃)");
        weatherData.setDayPictureUrl(dayPictureUrl+formatType(jsonObject.getString("type"))+".png");
        weatherData.setNightPictureUrl(nightPictureUrl+formatType(jsonObject.getString("type"))+".png");
        weatherData.setWeather(jsonObject.getString("type"));
        weatherData.setWind(jsonObject.getString("fengxiang")+formatFengli(jsonObject.getString("fengli")));
        weatherData.setTemperature(formatTemperature(jsonObject.getString("high"))+" ~ "+jsonObject.getString("low").substring(3));
    }

    //获取之后的天气预报数据
    public void getNextWeather(WeatherData weatherData,JSONObject jsonObject){
        weatherData.setDate(formatDate(jsonObject.getString("date")));
        weatherData.setDayPictureUrl(dayPictureUrl+formatType(jsonObject.getString("type"))+".png");
        weatherData.setNightPictureUrl(nightPictureUrl+formatType(jsonObject.getString("type"))+".png");
        weatherData.setWeather(jsonObject.getString("type"));
        weatherData.setWind(jsonObject.getString("fengxiang")+formatFengli(jsonObject.getString("fengli")));
        weatherData.setTemperature(formatTemperature(jsonObject.getString("high"))+" ~ "+jsonObject.getString("low").substring(3));
    }


    //格式化时间（17日星期五-------周五）
    public String formatDate(String date){
        String str = date.substring(date.length() - 1);
        if("天".equals(str)){
            str="日";
        }
        return "周"+str;
    }

    //天气转拼音（多云-------duoyun）
    public String formatType(String type){
        String str = JsonUtils.ToPinyin(type);
        return str;
    }

    //格式化温度（低温 15℃-------15）
    public String formatTemperature(String str){
        str = str.substring(3);
        str = str.substring(0, str.length() - 1);
        return str;
    }

    //格式化风力（<![CDATA[<3级]]>-------微风）
    public String formatFengli(String fengli){
        fengli = fengli.substring(9);
        fengli = fengli.substring(0, fengli.length() - 3);
        if("<3级".equals(fengli)){
            fengli="微风";
        }
        return fengli;
    }

    //比较和当前时间的时间差（10位数时间戳）
    public Integer timeDifference(Integer updateTime){
        //获取当前时间戳
        long timeStampSec = System.currentTimeMillis()/1000;
        String timestamp = String.format("%010d", timeStampSec);
        //3.判断请求间隔时间是否有15分钟
        int time=Integer.parseInt(timestamp) - updateTime;
        return time;
    }


}
