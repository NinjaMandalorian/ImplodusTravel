package me.ninjamandalorian.ImplodusTravel.object;

import me.ninjamandalorian.ImplodusTravel.exceptions.ChatSettingException;

public interface ChatSettable {
    
    public boolean setSetting(String setting, Object value) throws ChatSettingException;

}
