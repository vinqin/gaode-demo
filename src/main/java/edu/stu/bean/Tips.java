package edu.stu.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "tips")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tips implements Serializable {

    private final static long serialVersionUID = 1L;

    @XmlElement(name = "tip")
    private List<Tip> tips;

    public List<Tip> getTips() {
        return tips;
    }

    public void setTips(List<Tip> tips) {
        this.tips = tips;
    }

    @Override
    public String toString() {
        return "Tips{" +
                "tips=" + tips +
                '}';
    }
}
