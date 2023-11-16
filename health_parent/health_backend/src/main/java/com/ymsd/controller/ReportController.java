package com.ymsd.controller;

import com.ymsd.constant.MessageConstant;
import com.ymsd.entity.Result;
import com.ymsd.service.IMemberService;
import com.ymsd.service.ISetMealService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {


    @Resource
    private IMemberService memberService;

    @Value("${report_put_path}")
    private String report_put_path;

    @Resource
    private ISetMealService setMealService;

    @GetMapping("/getMemberReport")
    public Result getMemberReport() {
        try {
            //针对于会员统计这一块 需求是当前月份以及前11个月的数据  年度的会员情况
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -12);//获得当前日期之前12个月的日期
            //转载数据 需要创建集合对象去存储前端所需要的月份以及每月对应的新的会员 数量
            ArrayList<String> monthList = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONTH, 1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                String yearMonth = sdf.format(calendar.getTime());
                monthList.add(yearMonth);
            }

            //map存放数据
            HashMap<String, Object> map = new HashMap<>();
            map.put("months", monthList);

            //查询每个月 所对应的会员数量
            List<Integer> monthCounts = memberService.findMemberCountByMonth(monthList);
            map.put("memberCount", monthCounts);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }


    }

    @GetMapping("/getSetmealReport")
    public Result getSetmealReport() {
        Map map = null;
        try {
            map = new HashMap<>();
            //一个数据为一个map，多个map存放arrayList里面
            ArrayList<Map<String, Object>> list = setMealService.findSetmealCount();
            map.put("setmealCount", list);
            //使用循环取出setmealName
            ArrayList<String> setmealNames = new ArrayList<>();
            for (Map<String, Object> m : list) {
                String name = (String) m.get("name");
                setmealNames.add(name);

            }
            map.put("setmealNames", setmealNames);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);

        }
    }


    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> map = setMealService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    @GetMapping("/exportBusinessReportExcel")
    public Result exportBusinessReportExcel(HttpServletResponse response) throws Exception {
        Map<String, Object> result = setMealService.getBusinessReportData();
        String reportDate = (String) result.get("reportDate");
        Integer todayNewMember = (Integer) result.get("todayNewMember");
        Integer totalMember = (Integer) result.get("totalMember");
        Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
        Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
        Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
        Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
        Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
        Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
        Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
        Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
        List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

        String templatePath = report_put_path + "report_template.xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(templatePath)));
        XSSFSheet sheet = workbook.getSheetAt(0);

        XSSFRow row = sheet.getRow(2);
        row.getCell(5).setCellValue(reportDate);//日期

        row = sheet.getRow(4);
        row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
        row.getCell(7).setCellValue(totalMember);//总会员数

        row = sheet.getRow(5);
        row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
        row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

        row = sheet.getRow(7);
        row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
        row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

        row = sheet.getRow(8);
        row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
        row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

        row = sheet.getRow(9);
        row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
        row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数


        for (int i = 0; i < hotSetmeal.size(); i++) {
            BigDecimal proportion = (BigDecimal) hotSetmeal.get(i).get("proportion");
            row = sheet.getRow(12 + i);
            row.getCell(4).setCellValue((String) hotSetmeal.get(i).get("name"));
            row.getCell(5).setCellValue((long) hotSetmeal.get(i).get("setmeal_count"));
            row.getCell(6).setCellValue(proportion.doubleValue());
        }


//            FileOutputStream fileOut = new FileOutputStream(report_put_path + "11.xlsx");
//            workbook.write(fileOut);
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("conten-Disposition", "attachment;filename=report.xlsx");
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
//            fileOut.close();
        workbook.close();
        return null;


    }

    @GetMapping("/exportBusinessReportPDF")
    public Result exportBusinessReportPDF(HttpServletResponse response, HttpServletRequest request) throws Exception {
        Map<String, Object> result = setMealService.getBusinessReportData();


        //取出返回结果数据，准备将报表数据写入到pdf文件中
        List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
        //动态获取模板文件绝对路径
        String jrxmlPath = report_put_path + "health_business3.jrxml";
        String jasperPath = report_put_path + "health_business3.jasper";
        //编译模板
        JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);

        //填充数据----使用JavaBean数据源方式填充
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, result, new JRBeanCollectionDataSource(hotSetmeal));

        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/pdf");
        response.setHeader("conten-Disposition", "attachment;filename=report.pdf");

        //输出文件
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);
        out.flush();
        out.close();

        return null;
    }

}
