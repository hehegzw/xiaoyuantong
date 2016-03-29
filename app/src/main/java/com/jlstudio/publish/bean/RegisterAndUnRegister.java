package com.jlstudio.publish.bean;


import java.util.List;
import com.jlstudio.group.bean.Contacts;
/**
 * Created by gzw on 2015/11/25.
 */
public class RegisterAndUnRegister {
    public List<Contacts> register;
    public List<Contacts> unRegister;
    public int UnRegCount;

    public RegisterAndUnRegister(List<Contacts> register, List<Contacts> unRegister,int UnRegCount) {
        this.register = register;
        this.unRegister = unRegister;
        this.UnRegCount = UnRegCount;
    }
}
