package com.dreamhouse.emp.controller;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dreamhouse.emp.model.EmpVO;
import com.dreamhouse.emp.model.EmpService;
import java.util.List;

@Controller
@RequestMapping("/emp")
public class EmpController {

    @Autowired
    EmpService empSvc;

    /* 新增頁面 */
    @GetMapping("addEmp")
    public String addEmp(ModelMap model) {
        EmpVO empVO = new EmpVO();
        model.addAttribute("empVO", empVO);
        return "back-end/emp/addEmp";
    }

    /* 新增提交 */
    @PostMapping("insert")
    public String insert(@Valid EmpVO empVO, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "back-end/emp/addEmp";
        }
        empSvc.addEmployee(empVO);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/emp/listAllEmp";
    }

    /* 查詢單筆，準備修改 */
    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("employeeId") String employeeId, ModelMap model) {
        EmpVO empVO = empSvc.getOneEmployee(Integer.valueOf(employeeId));
        model.addAttribute("empVO", empVO);
        return "back-end/emp/update_emp_input";
    }

    /* 修改提交 */
    @PostMapping("update")
    public String update(@Valid EmpVO empVO, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "back-end/emp/update_emp_input";
        }
        empSvc.updateEmployee(empVO);
        model.addAttribute("success", "- (修改成功)");
        empVO = empSvc.getOneEmployee(empVO.getEmployeeId());
        model.addAttribute("empVO", empVO);
        return "back-end/emp/listOneEmp";
    }

    /* 查詢頁面 */
    @GetMapping("select_page")
    public String select_page(Model model) {
        return "back-end/emp/select_page";
    }

    /* 查詢全部員工 */
    @GetMapping("listAllEmp")
    public String listAllEmp(Model model) {
        return "back-end/emp/listAllEmp";
    }

    /* 提供員工清單給 select_page.html 與 listAllEmp.html 使用 */
    @ModelAttribute("empListData")
    protected List<EmpVO> referenceListData(Model model) {
        return empSvc.getAllEmployees();
    }
}