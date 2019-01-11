package edu.stu.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@Deprecated
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchResult implements Serializable {

    private final static long serialVersionUID = 1L;

    private String status;

    private String count;

    private String info;

    private String infocode;

    private List<Tip> tips;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public List<Tip> getTips() {
        return tips;
    }

    public void setTips(List<Tip> tips) {
        this.tips = tips;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "status='" + status + '\'' +
                ", count='" + count + '\'' +
                ", info='" + info + '\'' +
                ", infocode='" + infocode + '\'' +
                ", tips=" + tips +
                '}';
    }
}
