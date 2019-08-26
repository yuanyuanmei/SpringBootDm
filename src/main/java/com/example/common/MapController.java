package com.elan.bg.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.elan.bg.constants.ConstantsBase;
import com.elan.bg.constants.ConstantsMsg;
import com.elan.bg.constants.ConstantsUrl;
import com.elan.bg.dto.*;
import com.elan.bg.model.MapInfoBean;
import com.elan.bg.service.MapInfoService;
import com.elan.bg.service.impl.MapInfoServiceImpl;
import com.elan.bg.utils.DozerUtils;
import com.elan.bg.utils.ServletUtils;
import com.elan.bg.utils.StringUtils;
import com.elan.token.anmiation.ValidateBean;
import com.elan.token.controller.BaseController;
import com.job1001.util.JSONFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class MapController extends BaseController {

    private MapInfoService mapInfoService;

    public MapController(){
        mapInfoService = new MapInfoServiceImpl();
    }

    /**
     * 根据关键字搜索地名列表
     * @param info
     * @return
     */
    @ValidateBean(SuggestionDto.class)
    public JSONObject suggestion(String info){
        SuggestionDto param = JSONObject.parseObject(info,SuggestionDto.class);
        //接收参数外的值
        MapInfoBean paramInfo = new MapInfoBean(param.getMmId(),param.getMmSrc());
        param.setMmId(null);param.setMmSrc(null);
        ResponseDto resp =ServletUtils.getBaiDuApi(param,ConstantsUrl.SUGGESTION_URL);

        if(resp.getStatus() == 0 ) {
            MapInfoBean mapInfoBean = DozerUtils.instance().map(resp.getResult().get(0), MapInfoBean.class);
            mapInfoBean.setLng(mapInfoBean.getLocation().getLng());
            mapInfoBean.setLat(mapInfoBean.getLocation().getLat());
            mapInfoBean.setMmId(paramInfo.getMmId());
            mapInfoBean.setMmSrc(paramInfo.getMmSrc());
            mapInfoBean.setId(mapInfoService.saveDm(mapInfoBean));
            return JSONFormatUtils.success(ConstantsMsg.GET_SUCCESS_MSG, mapInfoBean);
        }

        return JSONFormatUtils.fail(resp.getMessage());
    }

    /**
     * 地理编码接口
     * @param info
     * @return
     */
    @ValidateBean(GeocodingDto.class)
    public JSONObject geocoding(String info){
        GeocodingDto param = JSONObject.parseObject(info,GeocodingDto.class);
        //接收参数外的值
        String baiDuUrl = ServletUtils.getBaiDuUrl(param, ConstantsUrl.GEOCODING_URL);
        JSONObject result = JSONObject.parseObject(ServletUtils.get(baiDuUrl));
        if(result.getInteger("status") == 0 ) {
            LocationDto location = DozerUtils.instance().
                    map(JSONObject.parseObject(result.get("result").toString()).get("location") , LocationDto.class);
            MapInfoBean mapInfoBean = new MapInfoBean(param.getMmId(),param.getMmSrc()
                    ,param.getAddress(),location.getLat(),location.getLng());
            mapInfoBean.setId(mapInfoService.saveDm(mapInfoBean));
            return JSONFormatUtils.success(ConstantsMsg.GET_SUCCESS_MSG, mapInfoBean);
        }
        return JSONFormatUtils.fail(ConstantsMsg.GET_FAIL_MSG);
    }

    /**
     * 批量处理地理编码
     */
    /**
     * 地理编码接口
     * @param info
     * @return
     */
    public JSONObject batch(String info){
        List<GeocodingDto> geocodingDtos = JSONArray.parseArray(info, GeocodingDto.class);

        //接收参数外的值
//        String baiDuUrl = ServletUtils.getBaiDuUrl(param, ConstantsUrl.GEOCODING_URL);
//        JSONObject result = JSONObject.parseObject(ServletUtils.get(baiDuUrl));
//        if(result.getInteger("status") == 0 ) {
//            LocationDto location = DozerUtils.instance().
//                    map(JSONObject.parseObject(result.get("result").toString()).get("location") , LocationDto.class);
//            MapInfoBean mapInfoBean = new MapInfoBean(param.getMmId(),param.getMmSrc()
//                    ,param.getAddress(),location.getLat(),location.getLng());
//            mapInfoBean.setId(mapInfoService.saveDm(mapInfoBean));
//            return JSONFormatUtils.success(ConstantsMsg.GET_SUCCESS_MSG, mapInfoBean);
//        }
        return JSONFormatUtils.fail(ConstantsMsg.GET_FAIL_MSG);
    }


    /**
     * 以地点为原点进行范围检索
     * @param info
     * @return
     */
    @ValidateBean(SearchDto.class)
    public JSONObject search(String info){
        SearchDto param = JSONObject.parseObject(info,SearchDto.class);
        Map paramMap = DozerUtils.instance().map(param, Map.class);
        StringBuffer sbuid = new StringBuffer();
        sbuid = getSbuid(param.getPage_num(), param.getPage_size(), paramMap, sbuid);
        return JSONFormatUtils.success(ConstantsMsg.GET_SUCCESS_MSG,sbuid);
    }

    /**
     * 地点规划
     * @param info
     * @return
     */
    @ValidateBean(PlanDto.class)
    public JSONObject plan(String info){
        PlanDto param = JSONObject.parseObject(info,PlanDto.class);
        //获取地点数量，后面分组,需添加转义符
        int plan_length = param.getOrigins().split("\\|").length;
        ResponseDto resp = ServletUtils.getBaiDuApi(param, ConstantsUrl.WALKING_URL);
        if(Objects.nonNull(resp) && resp.getStatus() == 0){
            //获取路程结果对象集合
            List<RouteDto> routeList = resp.getResult().stream().map(item ->
                    DozerUtils.instance().map(item, RouteDto.class)).collect(Collectors.toList());
            List<List<RouteDto>> groupList = new ArrayList<>();
            int i = 1;
            List<RouteDto> childList = new ArrayList<>();
            //按照顺序分好组
            for(RouteDto item :routeList){
                //if(item.getDistance().getValue() > 0){
                    childList.add(item);
                    if(i % plan_length == 0 ){
                        groupList.add(childList);
                        childList = new ArrayList<>();
                    }
                //}
                i++;
            };

            Object[] group_num = groupList.toArray();
            //根据分组排序
            String plan_sort = plan_sort(group_num, "0");
            return JSONFormatUtils.success(ConstantsMsg.GET_SUCCESS_MSG,plan_sort);
        }
        return JSONFormatUtils.fail(resp.getMessage());
    }

    /**
     * 给地点顺序排序
     * @param group_num
     * @param plan_sort_str
     * @return
     */
    private String plan_sort(Object[] group_num, String plan_sort_str){
        String[] exist_sort = plan_sort_str.split(",");
        List<RouteDto> route_list = (List<RouteDto>) group_num[NumberUtils.toInt(exist_sort[exist_sort.length-1])];
        Object[] route_num = route_list.toArray();
        int min_length = 100 * 1000;
        int min_index = 0;
        for(int route_index = 0; route_index< route_num.length; route_index ++ ){
            RouteDto routeDto = (RouteDto) route_num[route_index];
            if(Arrays.asList(exist_sort).contains(route_index+"")){
                continue;
            }
            if(min_length > routeDto.getDistance().getValue()){
                min_length = routeDto.getDistance().getValue();
                min_index = route_index;
            }
        }
        if(exist_sort.length < group_num.length){
            plan_sort_str += plan_sort_str.length() == 0 ? min_index:","+min_index;
            return plan_sort(group_num, plan_sort_str);
        }
        return plan_sort_str;
    }
    /**
     * 分页显示，所以要循环添加
     * @param page_num
     * @param page_size
     * @param paramMap
     * @param sbuid
     * @return
     */
    private StringBuffer getSbuid(int page_num,int page_size,Map paramMap,StringBuffer sbuid){
        try {
            paramMap.put("page_num",page_num);
            String paramUrl = StringUtils.map2String(paramMap);
            JSONObject result = JSONObject.parseObject(ServletUtils.get(ConstantsUrl.SEARCHER_URL + paramUrl));
            ResponseDto resp = DozerUtils.instance().map(result, ResponseDto.class);
            if(resp.getStatus() == 0){
                //总页数
                int pageNums = resp.getTotal()% page_size > 0
                        ? resp.getTotal() / page_size + 1 : resp.getTotal() / page_size;

                resp.getResults().forEach(item->{
                    sbuid.append(sbuid.length() == 0 ? item.getString("uid"):","+item.getString("uid"));
                });

                if(page_num < pageNums-1){
                    page_num++;
                    getSbuid(page_num,page_size,paramMap,sbuid);
                }
            }
            return sbuid;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }










}
