package gr.hua.dit.rentEstate.controllers;

import gr.hua.dit.rentEstate.entities.Owner;
import gr.hua.dit.rentEstate.service.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"owner"})
public class OwnerController {
    private OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @RequestMapping
    public String showOwner(Model model) {
        model.addAttribute("owner", ownerService.getOwners());
        return "owner/owners";
    }

    @GetMapping({"/{id}"})
    public String showOwner(@PathVariable Integer id, Model model) {
        Owner owner = this.ownerService.getOwner(id);
        model.addAttribute("owners", owner);
        return "owner/owners";
    }

    @GetMapping({"/new"})
    public String addOwner(Model model) {
        Owner owner = new Owner();
        model.addAttribute("owner", owner);
        return "owner/owner";
    }

    @PostMapping({"/new"})
    public String saveOwner(@ModelAttribute("estate") Owner owner, Model model) {
        this.ownerService.saveOwner(owner);
        model.addAttribute("owners", this.ownerService.getOwners());
        return "owner/owners";
    }
}
