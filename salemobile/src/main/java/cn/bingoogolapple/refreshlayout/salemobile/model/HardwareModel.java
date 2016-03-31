package cn.bingoogolapple.refreshlayout.salemobile.model;
import com.parse.ParseClassName;
/**
 * Created by tchl on 10/19/15.
 */

@ParseClassName("HardwareModel")
public class HardwareModel {
    private String Ob;
    private String Bu; //Building
    private String Ro; //Room

    private String Eq; //Equipment
    private String Ph; //Phone
    private String Ac; //Accepter
    private String Ap; //AccepterPhone
    private String Em; //IsEmergency
    private String St; //Status
    private String Other; //other





    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return  "building:"+Bu+" Room"+Ro;//+"  Equipment:"+Eq+" Phone:"+Ph+" Accepter:"+Ac+" Status:"+St+" AccepterPhone"+Ap+" other:"+other;*/
    }

    public HardwareModel() {
        // TODO Auto-generated constructor stub
    }
    public HardwareModel(String ObjectId,String Bu,String Ro,String Eq,String Ph,String Ac,String Ap,String Em,String St,String other) {
 //   public HardwareModel(String Bu,String Ro) {
        super();
        this.Ob = ObjectId;
        this.Bu = Bu ;
        this.Ro = Ro;
        this.Eq = Eq;
        this.Ph = Ph;
        this.Ac = Ac;
        this.Ap = Ap;
        this.Em = Em;
        this.St = St;
        this.Other = other;
    }
    public void setOb(String ob) {
        this.Ob = ob;
    }
    public String getOb() {
        return Ob;
    }

    public void setBu(String bu) {
        this.Bu = bu;
    }
    public String getBu() {
        return Bu;
    }

    public void setRo(String ro) {
        this.Ro = ro;
    }
    public String getRo() {
        return Ro;
    }

    public void setEq(String eq) {
        this.Eq = eq;
    }
    public String getEq() {
        return Eq;
    }

    public void setPh(String ph) {
        this.Ph = ph;
    }
    public String getPh() {
        return Ph;
    }

    public void setAc(String ac) {
        this.Ac = ac;
    }
    public String getAc() {
        return Ac;
    }

    public void setAp(String ap) {
        this.Ap = ap;
    }
    public String getAp() {
        return Ap;
    }

    public void setEm(String em) {
        this.Em = em;
    }
    public String getEm() {
        return Em;
    }

    public void setSt(String st) {
        this.St = st;
    }
    public String getSt() {
        return St;
    }

    public void setOther(String other) {
        this.Other = other;
    }
    public String getOther() {
        return Other;
    }
}
