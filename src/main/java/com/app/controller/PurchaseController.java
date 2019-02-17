package com.app.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.app.model.Purchase;
import com.app.service.IPurchaseService;
import com.app.service.IShipmentTypeService;
import com.app.service.IWhUserTypeService;
import com.app.validator.PurchaseValidator;
import com.app.view.PurchaseExcelView;
import com.app.view.PurchasePdfView;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

	@Autowired
	private IPurchaseService purchaseService;
	@Autowired
	private IWhUserTypeService whUserTypeService;
	@Autowired
	private IShipmentTypeService shipmentTypeService;
	@Autowired
	private PurchaseValidator validator;

	@RequestMapping("/register")
	public String showRegister(ModelMap map) {
		map.addAttribute("purchase", new Purchase());
		map.addAttribute("whUserType", whUserTypeService.getAllWhUserByType("VENDOR"));
		map.addAttribute("shipmentType", shipmentTypeService.getEnableShipmentIdsAndCodes());
		return "PurchaseRegister";
	}

	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public String savePurchase(@ModelAttribute Purchase purchase,Errors errors,ModelMap map) {

		validator.validate(purchase, errors);

		if (errors.hasErrors()) {
			map.addAttribute("message", "please check all fields !!");

		} else {
			map.addAttribute("message", "Purchase is saved with Id :"+purchaseService.savePurchase(purchase));
			map.addAttribute("purchase", new Purchase());
		}
		map.addAttribute("whUserType", whUserTypeService.getAllWhUserByType("VENDOR"));
		map.addAttribute("shipmentType", shipmentTypeService.getEnableShipmentIdsAndCodes());
		return "PurchaseRegister";
	}

	/*@RequestMapping("/viewAll")
	public String viewAll(ModelMap map) {

		map.addAttribute("purchase", purchaseService.getAllPurchases());
		return "PurchaseData";
	}*/

	@RequestMapping("/view")
	public String viewOne(@RequestParam(required=false,defaultValue="0") Integer orderId,ModelMap map) {

		String page=null;
		if (orderId!=0) {
			map.addAttribute("purchase", purchaseService.getPurchaseById(orderId));
			page="PurchaseView";
		} else {
			map.addAttribute("purchase", purchaseService.getAllPurchases());
			page = "PurchaseData";

		}
		return page;
	}

	@RequestMapping("/delete")
	public String deletePurchase(@RequestParam Integer orderId,ModelMap map) {

		purchaseService.deletePurchase(orderId);
		map.addAttribute("message", "Purchase deleted successfully with id :"+orderId+" !!");
		map.addAttribute("purchase", purchaseService.getAllPurchases());
		return "PurchaseData";
	}

	@RequestMapping("/edit")
	public String editOne(@RequestParam Integer orderId,ModelMap map) {
		map.addAttribute("purchase", purchaseService.getPurchaseById(orderId));
		map.addAttribute("whUserType", whUserTypeService.getAllWhUserByType("VENDOR"));
		map.addAttribute("shipmentType", shipmentTypeService.getEnableShipmentIdsAndCodes());
		return "PurchaseEdit";
	}

	@RequestMapping(value="/update",method=RequestMethod.POST)
	public String updatePurchase(@ModelAttribute Purchase purchase,Errors errors,ModelMap map) {

		purchaseService.updatePurchase(purchase);
		map.addAttribute("purchase", purchaseService.getAllPurchases());
		return "PurchaseData";
		
	}

	@RequestMapping("/excelExport")
	public ModelAndView excelExport(@RequestParam(required=false,defaultValue="0") Integer orderId,ModelMap map) {
		ModelAndView mv=null;
		if (orderId!=0) {
			mv=new ModelAndView(new PurchaseExcelView(), "purchase", Arrays.asList(purchaseService.getPurchaseById(orderId)));
		} else {
			mv=new ModelAndView(new PurchaseExcelView(), "purchase", purchaseService.getAllPurchases());
		}
		return mv;
	}
	@RequestMapping("/pdfExport")
	public ModelAndView pdfExport(@RequestParam(required=false,defaultValue="0") Integer orderId,ModelMap map) {
		ModelAndView mv=null;
		if (orderId!=0) {
			mv=new ModelAndView(new PurchasePdfView(), "purchase", Arrays.asList(purchaseService.getPurchaseById(orderId)));
		} else {
			mv=new ModelAndView(new PurchasePdfView(), "purchase", purchaseService.getAllPurchases());
		}
		return mv;
	}
}
