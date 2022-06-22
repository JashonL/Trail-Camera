package com.shuoxd.camera.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class CameraBean implements MultiItemEntity {

    public CameraInfo camera;

    public LastPhoto lastPhoto;


    public String totalPhotoNum;
    public String noReadPhotoNum;


    public boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

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

    @Override
    public int getItemType() {
        return Integer.parseInt(camera.getIsNew());
    }

    public static class CameraInfo {

        private String id;
        private String userId;
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
        private String newFwVersion;
        private String lastUpdateTimeText;

        private String newModemFwVersion;

        private String isNew;

        private CameraParamter cameraParamter;


        public String getIsNew() {
            return isNew;
        }

        public void setIsNew(String isNew) {
            this.isNew = isNew;
        }

        public String getNewModemFwVersion() {
            return newModemFwVersion;
        }

        public void setNewModemFwVersion(String newModemFwVersion) {
            this.newModemFwVersion = newModemFwVersion;
        }

        public String getNewFwVersion() {
            return newFwVersion;
        }

        public void setNewFwVersion(String newFwVersion) {
            this.newFwVersion = newFwVersion;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String user) {
            this.userId = user;
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

        public String getLastUpdateTimeText() {
            return lastUpdateTimeText;
        }

        public void setLastUpdateTimeText(String lastUpdateTimeText) {
            this.lastUpdateTimeText = lastUpdateTimeText;
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


        public CameraParamter getCameraParamter() {
            return cameraParamter;
        }

        public void setCameraParamter(CameraParamter cameraParamter) {
            this.cameraParamter = cameraParamter;
        }

        public static class CameraParamter {
            private String id;
            private String imei;
            private String alias;
            private String captureMode;
            private String photoResolution;
            private String burstShot;
            private String photoBurstInterval;
            private String videoResolution;
            private String videoLength;
            private String audioRecording;
            private String shotLag;
            private String pirSensitivity;
            private String operationTime;
            private String operationDay;
            private String operationDayText;
            private String operationStart;
            private String operationStop;
            private String timelapseStart;
            private String timelapseStop;
            private String timelapseInterval;
            private String uploadFrequency;
            private String dailySyncTime;
            private String transmitType;
            private String loopRecording;
            private String infoStamp;
            private String timeFormat;
            private String timeZone;
            private String temperatureFormat;
            private String lcdDuringON;
            private String formatCard;
            private String fota;
            private String mcuFOTAPath;
            private String modemFOTAPath;
            private String longitude;
            private String latitude;


            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getCaptureMode() {
                return captureMode;
            }

            public void setCaptureMode(String captureMode) {
                this.captureMode = captureMode;
            }

            public String getPhotoResolution() {
                return photoResolution;
            }

            public void setPhotoResolution(String photoResolution) {
                this.photoResolution = photoResolution;
            }

            public String getBurstShot() {
                return burstShot;
            }

            public void setBurstShot(String burstShot) {
                this.burstShot = burstShot;
            }

            public String getPhotoBurstInterval() {
                return photoBurstInterval;
            }

            public void setPhotoBurstInterval(String photoBurstInterval) {
                this.photoBurstInterval = photoBurstInterval;
            }

            public String getVideoResolution() {
                return videoResolution;
            }

            public void setVideoResolution(String videoResolution) {
                this.videoResolution = videoResolution;
            }

            public String getVideoLength() {
                return videoLength;
            }

            public void setVideoLength(String videoLength) {
                this.videoLength = videoLength;
            }

            public String getAudioRecording() {
                return audioRecording;
            }

            public void setAudioRecording(String audioRecording) {
                this.audioRecording = audioRecording;
            }

            public String getShotLag() {
                return shotLag;
            }

            public void setShotLag(String shotLag) {
                this.shotLag = shotLag;
            }

            public String getPirSensitivity() {
                return pirSensitivity;
            }

            public void setPirSensitivity(String pirSensitivity) {
                this.pirSensitivity = pirSensitivity;
            }

            public String getOperationTime() {
                return operationTime;
            }

            public void setOperationTime(String operationTime) {
                this.operationTime = operationTime;
            }

            public String getOperationDay() {
                return operationDay;
            }

            public void setOperationDay(String operationDay) {
                this.operationDay = operationDay;
            }

            public String getOperationDayText() {
                return operationDayText;
            }

            public void setOperationDayText(String operationDayText) {
                this.operationDayText = operationDayText;
            }

            public String getOperationStart() {
                return operationStart;
            }

            public void setOperationStart(String operationStart) {
                this.operationStart = operationStart;
            }

            public String getOperationStop() {
                return operationStop;
            }

            public void setOperationStop(String operationStop) {
                this.operationStop = operationStop;
            }

            public String getTimelapseStart() {
                return timelapseStart;
            }

            public void setTimelapseStart(String timelapseStart) {
                this.timelapseStart = timelapseStart;
            }

            public String getTimelapseStop() {
                return timelapseStop;
            }

            public void setTimelapseStop(String timelapseStop) {
                this.timelapseStop = timelapseStop;
            }

            public String getTimelapseInterval() {
                return timelapseInterval;
            }

            public void setTimelapseInterval(String timelapseInterval) {
                this.timelapseInterval = timelapseInterval;
            }

            public String getUploadFrequency() {
                return uploadFrequency;
            }

            public void setUploadFrequency(String uploadFrequency) {
                this.uploadFrequency = uploadFrequency;
            }

            public String getDailySyncTime() {
                return dailySyncTime;
            }

            public void setDailySyncTime(String dailySyncTime) {
                this.dailySyncTime = dailySyncTime;
            }

            public String getTransmitType() {
                return transmitType;
            }

            public void setTransmitType(String transmitType) {
                this.transmitType = transmitType;
            }

            public String getLoopRecording() {
                return loopRecording;
            }

            public void setLoopRecording(String loopRecording) {
                this.loopRecording = loopRecording;
            }

            public String getInfoStamp() {
                return infoStamp;
            }

            public void setInfoStamp(String infoStamp) {
                this.infoStamp = infoStamp;
            }

            public String getTimeFormat() {
                return timeFormat;
            }

            public void setTimeFormat(String timeFormat) {
                this.timeFormat = timeFormat;
            }

            public String getTimeZone() {
                return timeZone;
            }

            public void setTimeZone(String timeZone) {
                this.timeZone = timeZone;
            }

            public String getTemperatureFormat() {
                return temperatureFormat;
            }

            public void setTemperatureFormat(String temperatureFormat) {
                this.temperatureFormat = temperatureFormat;
            }

            public String getLcdDuringON() {
                return lcdDuringON;
            }

            public void setLcdDuringON(String lcdDuringON) {
                this.lcdDuringON = lcdDuringON;
            }

            public String getFormatCard() {
                return formatCard;
            }

            public void setFormatCard(String formatCard) {
                this.formatCard = formatCard;
            }

            public String getFota() {
                return fota;
            }

            public void setFota(String fota) {
                this.fota = fota;
            }

            public String getMcuFOTAPath() {
                return mcuFOTAPath;
            }

            public void setMcuFOTAPath(String mcuFOTAPath) {
                this.mcuFOTAPath = mcuFOTAPath;
            }

            public String getModemFOTAPath() {
                return modemFOTAPath;
            }

            public void setModemFOTAPath(String modemFOTAPath) {
                this.modemFOTAPath = modemFOTAPath;
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
        private String fullPath;
        private String videoImgPath;
        private String fullVideoImgPath;

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


        public String getFullPath() {
            return fullPath;
        }

        public void setFullPath(String fullPath) {
            this.fullPath = fullPath;
        }

        public String getVideoImgPath() {
            return videoImgPath;
        }

        public void setVideoImgPath(String videoImgPath) {
            this.videoImgPath = videoImgPath;
        }


        public String getFullVideoImgPath() {
            return fullVideoImgPath;
        }

        public void setFullVideoImgPath(String fullVideoImgPath) {
            this.fullVideoImgPath = fullVideoImgPath;
        }
    }


}
