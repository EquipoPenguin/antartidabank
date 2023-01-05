package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.services.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class OTPController {
    @Autowired
    public OTPService otpService;
    //email service

    @GetMapping("/generateOtp")
    public String generateOTP() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        int otp = otpService.generateOTP(username);
        //Send email

        return "otppage";
    }

    @RequestMapping(value = "/validateOtp", method = RequestMethod.GET)
    public @ResponseBody String validateOtp(@RequestParam("otpnum") int otpnum) {
        final String success = "Entered OTP is valid";
        final String fail = "Entered OTP is NOT valid. Please Retry!";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (otpnum >= 0){
            int serverOtp = otpService.getOtp(username);
            if (serverOtp > 0) {
                if (otpnum == serverOtp) {
                    otpService.clearOTP(username);
                    return (success);
                } else {
                    return fail;
                }
            } else {
                return fail;
            }
        } else {
            return fail;
        }
    }



}
