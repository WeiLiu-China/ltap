package com.xdja.admin.bean;

import java.util.List;

public class RoamAppInfo {

    /** 应用id */
    private String appId;
    /** 应用所属网络区域 */
    private String appNetworkCode;
    /** 应用所属行政区划代码 */
    private String appRegionalismCode;
    /** 应用名称 */
    private String appName;
    /** 应用版本号 */
    private String appVersion;
    /** 应用版本值 */
    private String versionCode;
    /** 应用logoUrl */
    private String appLogoUrl;
    /** 应用描述 */
    private String appDescription;
    /** 应用所属类型 */
    private String appType;
    /** 应用访问路径（仅H5应用有） */
    private String appUrl;
    /** 应用包名 */
    private String appPackage;
    /** 应用文件的MD5值 */
    private String appFileMd5;
    /** 应用大小（KB） */
    private String appFileSize;
    /** 应用升级特性（如果非第一版，有该值） */
    private String updateNote;
    /** 应用图片列表 */
    private List<String> appPictures;
    /** 应用视频地址 */
    private String appVideoUrl;
    /** 应用视频缩略图 */
    private String appVideoImage;
    /** 创建时间戳 */
    private String createTime;
    /** 更新时间戳 */
    private String updateTime;


    public String getAppId() {
        return appId;
    }


    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppNetworkCode() {
        return appNetworkCode;
    }

    public void setAppNetworkCode(String appNetworkCode) {
        this.appNetworkCode = appNetworkCode;
    }

    public String getAppRegionalismCode() {
        return appRegionalismCode;
    }

    public void setAppRegionalismCode(String appRegionalismCode) {
        this.appRegionalismCode = appRegionalismCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getAppLogoUrl() {
        return appLogoUrl;
    }

    public void setAppLogoUrl(String appLogoUrl) {
        this.appLogoUrl = appLogoUrl;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getAppFileMd5() {
        return appFileMd5;
    }

    public void setAppFileMd5(String appFileMd5) {
        this.appFileMd5 = appFileMd5;
    }

    public String getAppFileSize() {
        return appFileSize;
    }

    public void setAppFileSize(String appFileSize) {
        this.appFileSize = appFileSize;
    }

    public String getUpdateNote() {
        return updateNote;
    }

    public void setUpdateNote(String updateNote) {
        this.updateNote = updateNote;
    }

    public List<String> getAppPictures() {
        return appPictures;
    }

    public void setAppPictures(List<String> appPictures) {
        this.appPictures = appPictures;
    }

    public String getAppVideoUrl() {
        return appVideoUrl;
    }

    public void setAppVideoUrl(String appVideoUrl) {
        this.appVideoUrl = appVideoUrl;
    }

    public String getAppVideoImage() {
        return appVideoImage;
    }

    public void setAppVideoImage(String appVideoImage) {
        this.appVideoImage = appVideoImage;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
