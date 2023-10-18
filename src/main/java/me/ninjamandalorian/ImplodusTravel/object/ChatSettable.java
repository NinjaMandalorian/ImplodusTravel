package me.ninjamandalorian.ImplodusTravel.object;

import me.ninjamandalorian.ImplodusTravel.exceptions.ChatSettingException;

/** ChatSettable interface
 * <p> Allows for the setting of several objects via chat messages
 * @author NinjaMandalorian
 */
public interface ChatSettable {
    
    /** Sets settings to a specific value
     * @param setting - Setting key / name
     * @param value - Object value to set
     * @return Success boolean
     * @throws ChatSettingException Error occurs setting value
     */
    public boolean setSetting(String setting, Object value) throws ChatSettingException;

}
