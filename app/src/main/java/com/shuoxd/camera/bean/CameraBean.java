package com.shuoxd.camera.bean;

public class CameraBean {

    public CameraInfo camera;

    public LastPhoto lastPhoto;



    public String totalPhotoNum;
    public String noReadPhotoNum;


    public String getTotalPhotoNum() {
        return totalPhotoNum;
    }

    public void setTotalPhotoNum(String totalPhotoNum) {
        this.totalPhotoNum = totalPhotoNum;
    }

    public String getNoReadPhotoNum() {
        return noReadPhotoNum;
    }

    public void setNoReadPhotoNum(String noReadPhotoNum) {
        this.noReadPhotoNum = noReadPhotoNum;
    }

    public CameraInfo getCamera() {
        return camera;
    }

    public void setCamera(CameraInfo camera) {
        this.camera = camera;
    }

    public LastPhoto getLastPhoto() {
        return lastPhoto;
    }

    public void setLastPhoto(LastPhoto lastPhoto) {
        this.lastPhoto = lastPhoto;
    }

    public static class CameraInfo {

        private String id;
        private String user;
        private String imei;
        private String alias;
        private String iccid;
        private String deviceModel;
        private String fwVersion;
        private String modemModel;
        private String modemFwVersion;
        private String batteryLevel;
        private String extDcLevel;
        private String cardSpace;
        private String signalStrength;
        private String scenePhoto;
        private String lastUpdateTime;
        private String longitude;
        private String latitude;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getIccid() {
            return iccid;
        }

        public void setIccid(String iccid) {
            this.iccid = iccid;
        }

        public String getDeviceModel() {
            return deviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        public String getFwVersion() {
            return fwVersion;
        }

        public void setFwVersion(String fwVersion) {
            this.fwVersion = fwVersion;
        }

        public String getModemModel() {
            return modemModel;
        }

        public void setModemModel(String modemModel) {
            this.modemModel = modemModel;
        }

        public String getModemFwVersion() {
            return modemFwVersion;
        }

        public void setModemFwVersion(String modemFwVersion) {
            this.modemFwVersion = modemFwVersion;
        }

        public String getBatteryLevel() {
            return batteryLevel;
        }

        public void setBatteryLevel(String batteryLevel) {
            this.batteryLevel = batteryLevel;
        }

        public String getExtDcLevel() {
            return extDcLevel;
        }

        public void setExtDcLevel(String extDcLevel) {
            this.extDcLevel = extDcLevel;
        }

        public String getCardSpace() {
            return cardSpace;
        }

        public void setCardSpace(String cardSpace) {
            this.cardSpace = cardSpace;
        }

        public String getSignalStrength() {
            return signalStrength;
        }

        public void setSignalStrength(String signalStrength) {
            this.signalStrength = signalStrength;
        }

        public String getScenePhoto() {
            return scenePhoto;
        }

        public void setScenePhoto(String scenePhoto) {
            this.scenePhoto = scenePhoto;
        }

        public String getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(String lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }
    }

    public static class LastPhoto {
        private String id;
        private String userId;
        private String imei;
        private String fileName;
        private String type;
        private String isRead;
        private String collection;
        private String path;
        private String uploadTime;
        private String uploadDate;
        private String uploadYear;
        private String uploadMonth;
        private String uploadDay;
        private String amPm;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIsRead() {
            return isRead;
        }

        public void setIsRead(String isRead) {
            this.isRead = isRead;
        }

        public String getCollection() {
            return collection;
        }

        public void setCollection(String collection) {
            this.collection = collection;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getUploadTime() {
            return uploadTime;
        }

        public void setUploadTime(String uploadTime) {
            this.uploadTime = uploadTime;
        }

        public String getUploadDate() {
            return uploadDate;
        }

        public void setUploadDate(String uploadDate) {
            this.uploadDate = uploadDate;
        }

        public String getUploadYear() {
            return uploadYear;
        }

        public void setUploadYear(String uploadYear) {
            this.uploadYear = uploadYear;
        }

        public String getUploadMonth() {
            return uploadMonth;
        }

        public void setUploadMonth(String uploadMonth) {
            this.uploadMonth = uploadMonth;
        }

        public String getUploadDay() {
            return uploadDay;
        }

        public void setUploadDay(String uploadDay) {
            this.uploadDay = uploadDay;
        }

        public String getAmPm() {
            return amPm;
        }

        public void setAmPm(String amPm) {
            this.amPm = amPm;
        }
    }


}
