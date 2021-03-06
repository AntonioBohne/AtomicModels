/* 
 * Copyright (C) 2018 Antonio---https://github.com/AntonioBohne
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package chemistry.dataModel;

import java.util.List;

/**
 * Data model that stores information about a Molecule. It stores a table
 * which contains the atoms that form the molecule and the amount of them.
 * @author https://github.com/AntonioBohne
 */
public class Molecule {

    /**
     * Describes the type of bond a molecule has.
     */
    public enum BOND_TYPE{
        COVALENT,
        COVALENT_POLAR,
        IONIC;
    }
    
    private List<AtomToken> atomTable;
    private BOND_TYPE bond;
}
