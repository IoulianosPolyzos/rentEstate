package gr.hua.dit.rentEstate.controllers;

import gr.hua.dit.rentEstate.entities.Tenant;
import gr.hua.dit.rentEstate.service.TenantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("tenant")
public class TenantController {


    TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping("")
    public String showTenants(Model model){
        model.addAttribute("tenants", tenantService.getTenants());
        return "tenant/tenants";
    }

    @GetMapping("/{id}")
    public String showTenant(@PathVariable Integer id, Model model){
        model.addAttribute("tenants", tenantService.getTenant(id));
        return "tenant/tenants";
    }

    @GetMapping("/profile/{id}")
    public String showProfile(@PathVariable Integer id, Model model){
        model.addAttribute("tenant", tenantService.getTenant(id));
        return "tenant/tenant-profile";
    }

    @GetMapping("/new")
    public String addTenant(Model model){
        Tenant tenant = new Tenant();
        model.addAttribute("tenant", tenant);
        return "tenant/tenant";
    }

    @PostMapping("/new")
    public String saveTenant(@ModelAttribute("tenant") Tenant tenant, Model model) {
        tenantService.saveTenant(tenant);
        model.addAttribute("tenants", tenantService.getTenants());
        return "tenant/tenants";
    }
}
