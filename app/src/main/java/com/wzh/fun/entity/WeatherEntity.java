package com.wzh.fun.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhihao.wen on 2016/11/23.
 */

public class WeatherEntity extends BaseEntity {
    @SerializedName("pubdate")
    private String pubdate;
    @SerializedName("pubtime")
    private String pubtime;
    @SerializedName("realtime")
    private RealtimeBean realtime;
    @SerializedName("life")
    private LifeBean life;
    @SerializedName("f3h")
    private F3hBean f3h;
    @SerializedName("pm25")
    private Pm25Bean pm25;
    @SerializedName("jingqu")
    private String jingqu;
    @SerializedName("jingqutq")
    private String jingqutq;
    @SerializedName("date")
    private String date;
    @SerializedName("isForeign")
    private String isForeign;

    @SerializedName("weather")
    private List<WeatherBean> weather;

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getPubtime() {
        return pubtime;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }

    public RealtimeBean getRealtime() {
        return realtime;
    }

    public void setRealtime(RealtimeBean realtime) {
        this.realtime = realtime;
    }

    public LifeBean getLife() {
        return life;
    }

    public void setLife(LifeBean life) {
        this.life = life;
    }

    public F3hBean getF3h() {
        return f3h;
    }

    public void setF3h(F3hBean f3h) {
        this.f3h = f3h;
    }

    public Pm25Bean getPm25() {
        return pm25;
    }

    public void setPm25(Pm25Bean pm25) {
        this.pm25 = pm25;
    }

    public String getJingqu() {
        return jingqu;
    }

    public void setJingqu(String jingqu) {
        this.jingqu = jingqu;
    }

    public String getJingqutq() {
        return jingqutq;
    }

    public void setJingqutq(String jingqutq) {
        this.jingqutq = jingqutq;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsForeign() {
        return isForeign;
    }

    public void setIsForeign(String isForeign) {
        this.isForeign = isForeign;
    }

    public List<WeatherBean> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherBean> weather) {
        this.weather = weather;
    }

    public static class RealtimeBean {
        @SerializedName("city_code")
        private String cityCode;
        @SerializedName("city_name")
        private String cityName;
        @SerializedName("date")
        private String date;
        @SerializedName("time")
        private String time;
        @SerializedName("week")
        private int week;
        @SerializedName("moon")
        private String moon;
        @SerializedName("dataUptime")
        private int dataUptime;
        /**
         * temperature : -5
         * humidity : 48
         * info : 多云
         * img : 1
         */

        @SerializedName("weather")
        private WeatherBean weather;
        /**
         * direct : 西风
         * power : 2级
         * offset : null
         * windspeed : null
         */

        @SerializedName("wind")
        private WindBean wind;

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getWeek() {
            return week;
        }

        public void setWeek(int week) {
            this.week = week;
        }

        public String getMoon() {
            return moon;
        }

        public void setMoon(String moon) {
            this.moon = moon;
        }

        public int getDataUptime() {
            return dataUptime;
        }

        public void setDataUptime(int dataUptime) {
            this.dataUptime = dataUptime;
        }

        public WeatherBean getWeather() {
            return weather;
        }

        public void setWeather(WeatherBean weather) {
            this.weather = weather;
        }

        public WindBean getWind() {
            return wind;
        }

        public void setWind(WindBean wind) {
            this.wind = wind;
        }

        public static class WeatherBean {
            @SerializedName("temperature")
            private String temperature;
            @SerializedName("humidity")
            private String humidity;
            @SerializedName("info")
            private String info;
            @SerializedName("img")
            private String img;

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getHumidity() {
                return humidity;
            }

            public void setHumidity(String humidity) {
                this.humidity = humidity;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }

        public static class WindBean {
            @SerializedName("direct")
            private String direct;
            @SerializedName("power")
            private String power;
            @SerializedName("offset")
            private Object offset;
            @SerializedName("windspeed")
            private Object windspeed;

            public String getDirect() {
                return direct;
            }

            public void setDirect(String direct) {
                this.direct = direct;
            }

            public String getPower() {
                return power;
            }

            public void setPower(String power) {
                this.power = power;
            }

            public Object getOffset() {
                return offset;
            }

            public void setOffset(Object offset) {
                this.offset = offset;
            }

            public Object getWindspeed() {
                return windspeed;
            }

            public void setWindspeed(Object windspeed) {
                this.windspeed = windspeed;
            }
        }
    }

    public static class LifeBean {
        @SerializedName("date")
        private String date;
        @SerializedName("info")
        private InfoBean info;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean {
            @SerializedName("chuanyi")
            private List<String> chuanyi;
            @SerializedName("ganmao")
            private List<String> ganmao;
            @SerializedName("kongtiao")
            private List<String> kongtiao;
            @SerializedName("xiche")
            private List<String> xiche;
            @SerializedName("yundong")
            private List<String> yundong;
            @SerializedName("ziwaixian")
            private List<String> ziwaixian;

            public List<String> getChuanyi() {
                return chuanyi;
            }

            public void setChuanyi(List<String> chuanyi) {
                this.chuanyi = chuanyi;
            }

            public List<String> getGanmao() {
                return ganmao;
            }

            public void setGanmao(List<String> ganmao) {
                this.ganmao = ganmao;
            }

            public List<String> getKongtiao() {
                return kongtiao;
            }

            public void setKongtiao(List<String> kongtiao) {
                this.kongtiao = kongtiao;
            }

            public List<String> getXiche() {
                return xiche;
            }

            public void setXiche(List<String> xiche) {
                this.xiche = xiche;
            }

            public List<String> getYundong() {
                return yundong;
            }

            public void setYundong(List<String> yundong) {
                this.yundong = yundong;
            }

            public List<String> getZiwaixian() {
                return ziwaixian;
            }

            public void setZiwaixian(List<String> ziwaixian) {
                this.ziwaixian = ziwaixian;
            }
        }
    }

    public static class F3hBean {
        /**
         * jg : 20161123080000
         * jb : -5
         */

        @SerializedName("temperature")
        private List<TemperatureBean> temperature;
        /**
         * jg : 20161123080000
         * jf : 0
         */

        @SerializedName("precipitation")
        private List<PrecipitationBean> precipitation;

        public List<TemperatureBean> getTemperature() {
            return temperature;
        }

        public void setTemperature(List<TemperatureBean> temperature) {
            this.temperature = temperature;
        }

        public List<PrecipitationBean> getPrecipitation() {
            return precipitation;
        }

        public void setPrecipitation(List<PrecipitationBean> precipitation) {
            this.precipitation = precipitation;
        }

        public static class TemperatureBean {
            @SerializedName("jg")
            private String jg;
            @SerializedName("jb")
            private String jb;

            public String getJg() {
                return jg;
            }

            public void setJg(String jg) {
                this.jg = jg;
            }

            public String getJb() {
                return jb;
            }

            public void setJb(String jb) {
                this.jb = jb;
            }
        }

        public static class PrecipitationBean {
            @SerializedName("jg")
            private String jg;
            @SerializedName("jf")
            private String jf;

            public String getJg() {
                return jg;
            }

            public void setJg(String jg) {
                this.jg = jg;
            }

            public String getJf() {
                return jf;
            }

            public void setJf(String jf) {
                this.jf = jf;
            }
        }
    }

    public static class Pm25Bean {
        @SerializedName("key")
        private String key;
        @SerializedName("show_desc")
        private int showDesc;
        /**
         * curPm : 35
         * pm25 : 15
         * pm10 : 37
         * level : 1
         * quality : 优
         * des : 可正常活动。
         */

        @SerializedName("pm25")
        private Pm25 pm25;
        @SerializedName("dateTime")
        private String dateTime;
        @SerializedName("cityName")
        private String cityName;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getShowDesc() {
            return showDesc;
        }

        public void setShowDesc(int showDesc) {
            this.showDesc = showDesc;
        }

        public Pm25 getPm25() {
            return pm25;
        }

        public void setPm25(Pm25 pm25) {
            this.pm25 = pm25;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public static class Pm25{
            @SerializedName("curPm")
            private String curPm;
            @SerializedName("pm25")
            private String pm25;
            @SerializedName("pm10")
            private String pm10;
            @SerializedName("level")
            private int level;
            @SerializedName("quality")
            private String quality;
            @SerializedName("des")
            private String des;

            public String getCurPm() {
                return curPm;
            }

            public void setCurPm(String curPm) {
                this.curPm = curPm;
            }

            public String getPm25() {
                return pm25;
            }

            public void setPm25(String pm25) {
                this.pm25 = pm25;
            }

            public String getPm10() {
                return pm10;
            }

            public void setPm10(String pm10) {
                this.pm10 = pm10;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public String getQuality() {
                return quality;
            }

            public void setQuality(String quality) {
                this.quality = quality;
            }

            public String getDes() {
                return des;
            }

            public void setDes(String des) {
                this.des = des;
            }
        }
    }

    public static class WeatherBean {
        @SerializedName("date")
        private String date;
        @SerializedName("info")
        private InfoBean info;
        @SerializedName("week")
        private String week;
        @SerializedName("nongli")
        private String nongli;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getNongli() {
            return nongli;
        }

        public void setNongli(String nongli) {
            this.nongli = nongli;
        }

        public static class InfoBean {
            @SerializedName("day")
            private List<String> day;
            @SerializedName("night")
            private List<String> night;

            public List<String> getDay() {
                return day;
            }

            public void setDay(List<String> day) {
                this.day = day;
            }

            public List<String> getNight() {
                return night;
            }

            public void setNight(List<String> night) {
                this.night = night;
            }
        }
    }
}
