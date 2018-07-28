package com.cg.walletApp.controllers;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.cg.walletApp.beans.Customer;
import com.cg.walletApp.beans.Transactions;
import com.cg.walletApp.exception.CustomerDetailsNotFound;
import com.cg.walletApp.exception.InsufficientBalanceException;
import com.cg.walletApp.exception.InvalidInputException;
import com.cg.walletApp.service.WalletService;

//controller are springBeans //@Controller to define bean
@Controller
@SessionAttributes("mobileNo")
@Scope("session")
public class CustomerActionController {

	@Autowired
	WalletService walletService;

	//url mapping //which method to call //bind this through action in registrationPage.jsp
	//called by dispatcher servlet(/) and forward to using controllers
	@RequestMapping(value="/registerCustomer")
	public ModelAndView registerCustomer(@Valid @ModelAttribute("customer") Customer customer, BindingResult result) {

		try {
			//send req to same page and display errors
			if(result.hasErrors()) return new ModelAndView("registrationPage");
			customer = walletService.createAccount(customer);
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("registrationSuccessPage", "customer", customer);		
	}

	//login customer and show balance
	@RequestMapping(value="/findCustomer")
	public ModelAndView findCustomer(@RequestParam("mobileNo") String mobileNo,HttpServletRequest request) {
		Customer customer = new Customer();
		try {
			request.getSession().setAttribute("mobileNo", mobileNo);
			customer = walletService.showBalance(mobileNo);
			return new ModelAndView("showBalanceSuccess", "customer", customer);
		} catch (CustomerDetailsNotFound e) {
			return new ModelAndView("loginPage","errorMessage",e.getMessage());
		}	
	}

	@RequestMapping(value="/depositAmt")
	public ModelAndView depositAmt(@RequestParam("wallet.balance") BigDecimal amount,HttpServletRequest request) {

		Customer customer = new Customer();
		try {
			String mobileNo=(String)request.getSession().getAttribute("mobileNo");
			customer = walletService.depositAmount(mobileNo, amount);
			return new ModelAndView("depositAmountSuccess", "customer", customer);
		} catch (InvalidInputException e) {
			return new ModelAndView("depositAmountPage","errorMessage",e.getMessage());
		}	
	}

	@RequestMapping(value="/withdrawAmount")
	public ModelAndView withdrawAmount(@RequestParam("wallet.balance") BigDecimal amount,HttpServletRequest request) {
		Customer customer = new Customer();
		try {
			String mobileNo=(String)request.getSession().getAttribute("mobileNo");
			customer = walletService.withdrawAmount(mobileNo, amount);
			return new ModelAndView("withdrawAmountSuccess", "customer", customer);	
		} catch (InvalidInputException | InsufficientBalanceException e) {
			return new ModelAndView("withdrawAmountPage","errorMessage",e.getMessage());
		}		
	}
	
	@RequestMapping(value="/fundTsf")
	public ModelAndView fundTsf(@RequestParam("sourceMobile") String sourceMobile, @RequestParam("targetMobile") String targetMobile, @RequestParam("wallet.balance") BigDecimal amount) {

		Customer customer = new Customer();

		try {
			customer = walletService.fundTransfer(sourceMobile, targetMobile, amount);
		} catch (InvalidInputException | InsufficientBalanceException e) {

			e.printStackTrace();
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("fundTransferSuccess", "customer", customer);	
	}	
	
	public String getMobileNo(@RequestParam(value="mobileNo",required=true)String mobileNo) {
		return mobileNo;
	}
	@RequestMapping(value="/printAllTransactions")
	public ModelAndView transactionCustomer(@RequestParam("mobileNo") String mobileNo) {
		List<Transactions>customer;
		try {
			//send req to same page and display errors
			customer = walletService.getAllTransactions(mobileNo);
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("transactionSuccessPage", "transactions1", customer);		
	}
	@RequestMapping(value="/getAllCustomers")
	public ModelAndView getAllCustomers() {

		List<Customer> customer ;

		try {
			customer = walletService.getAllCustomers();
		} catch (InvalidInputException | InsufficientBalanceException e) {

			e.printStackTrace();
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("getAllCustomers", "customer", customer);	
	}
	@RequestMapping(value="/getCustomers")
	public ModelAndView getCustomers() {

		List<Customer> customer ;

		try {
			customer = walletService.getCustomers();
		} catch (InvalidInputException | InsufficientBalanceException e) {

			e.printStackTrace();
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("customerPage", "customer", customer);	
	}
}
