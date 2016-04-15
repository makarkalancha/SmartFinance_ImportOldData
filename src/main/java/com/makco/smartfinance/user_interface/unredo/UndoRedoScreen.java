package com.makco.smartfinance.user_interface.unredo;

import com.makco.smartfinance.user_interface.unredo.Memento;

/**
 * User: Makar Kalancha
 * Date: 14/04/2016
 * Time: 21:46
 */
public interface UndoRedoScreen {
    void saveForm();
    void restoreFormState(Memento memento);
}
