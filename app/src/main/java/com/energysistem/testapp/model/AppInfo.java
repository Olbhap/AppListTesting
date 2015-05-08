package com.energysistem.testapp.model;
/**
 * Created by pgc on 08/05/2015.
 */
public class AppInfo {
    private String packageName,appName;
    private boolean aparece, instala,funciona;

    public AppInfo() {

    }
    public AppInfo(String _appName, String _packageName, boolean _aparece, boolean _instala, boolean _funciona)
    {
        this.appName=_appName;
        this.packageName=_packageName;
        this.aparece=_aparece;
        this.instala=_instala;
        this.funciona=_funciona;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isAparece() {
        return aparece;
    }

    public void setAparece(boolean aparece) {
        this.aparece = aparece;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isFunciona() {
        return funciona;
    }

    public void setFunciona(boolean funciona) {
        this.funciona = funciona;
    }

    public boolean isInstala() {
        return instala;
    }

    public void setInstala(boolean instala) {
        this.instala = instala;
    }
}
