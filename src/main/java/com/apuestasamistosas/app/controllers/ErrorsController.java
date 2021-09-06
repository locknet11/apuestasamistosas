package com.apuestasamistosas.app.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorsController implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {

        
        ModelAndView errorPage = new ModelAndView("error");
        String errorMsg;
        Integer httpErrorCode;
        
        if(getErrorCode(httpRequest) != null){
            httpErrorCode = getErrorCode(httpRequest);
        }else{
            httpErrorCode = 0;
        }
       
        switch (httpErrorCode) {
            case 400: {
                errorMsg = "El recurso solicitado no existe.";
                break;
            }
            case 403: {
                errorMsg = "No tiene permisos para acceder al recurso.";
                break;
            }
            case 401: {
                errorMsg = "No se encuentra autorizado.";
                break;
            }
            case 404: {
                errorMsg = "El recurso solicitado no fue encontrado.";
                break;
            }
            case 500: {
                errorMsg = "Ocurrió un error interno.";
                break;
            }
            
            default:
            	httpErrorCode = 500;
                errorMsg = "Ocurrio un error inesperado.";
        }

        errorPage.addObject("code", httpErrorCode);
        errorPage.addObject("message", errorMsg);
        return errorPage;
    }

    private Integer getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    }

    public String getErrorPath() {
        return "/error";
    }

}
