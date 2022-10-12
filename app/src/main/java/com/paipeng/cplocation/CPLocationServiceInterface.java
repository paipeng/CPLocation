package com.paipeng.cplocation;


public interface CPLocationServiceInterface {
    void setLanguage(CPBaseLocationService.CP_LOCATION_LANGUAGE language);
    void start();
    void stop();
    void searching();
    void started();
    void stopped();
}
