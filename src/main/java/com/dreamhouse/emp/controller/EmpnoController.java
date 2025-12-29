package com.dreamhouse.emp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import com.dreamhouse.emp.model.EmpVO;
import com.dreamhouse.emp.model.EmpService;

@Controller
@Validated
@RequestMapping("/emp")
public class EmpnoController {

    @Autowired
    EmpService empSvc;

    /* 查詢單筆（顯示，含輸入驗證） */
    @PostMapping("getOne_For_Display")
    public String getOne_For_Display(
        @NotEmpty(message="員工編號: 請勿空白")
        @Digits(integer = 5, fraction = 0, message = "員工編號: 請填數字-請勿超過{integer}位數")
        @Min(value = 1, message = "員工編號: 不能小於{value}")
        @Max(value = 99999, message = "員工編號: 不能超過{value}")
        @RequestParam("employeeId") String employeeId,
        ModelMap model) {

        // 查詢資料
        EmpVO empVO = empSvc.getOneEmployee(Integer.valueOf(employeeId));

        List<EmpVO> list = empSvc.getAllEmployees();
        model.addAttribute("empListData", list);

        if (empVO == null) {
            model.addAttribute("errorMessage", "查無資料");
            return "back-end/emp/select_page";
        }

        // 查詢完成，準備轉交
        model.addAttribute("empVO", empVO);
        return "back-end/emp/select_page"; 
        // 由 select_page.html insert listOneEmp.html 的 fragment 顯示結果
    }

    /* 輸入驗證錯誤處理 */
    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage()).append("<br>");
        }

        List<EmpVO> list = empSvc.getAllEmployees();
        model.addAttribute("empListData", list);

        String message = strBuilder.toString();
        return new ModelAndView("back-end/emp/select_page", "errorMessage", "請修正以下錯誤:<br>" + message);
    }
}