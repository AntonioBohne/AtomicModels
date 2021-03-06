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
package chemistry.game;

import chemistry.atoms.Atom;
import chemistry.resourceloader.LanguageLoader;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class provides functionality for atom based trivia games where the user
 * needs to guess a certain property from an atom. It provides an 
 * interactive difficulty level that changes based on the current score and 
 * provides the necessary methods to aid in the generation of questions.
 * @author https://github.com/AntonioBohne
 */
public abstract class AtomTriviaInterface extends TriviaInterface{
    
    /**
     * Stores the right answer for the round.
     */
    private Atom answer;
    protected void setAnswers(Atom answers){this.answer = answers;}
    protected Atom getAnswers(){return answer;}
    
 
    /**
     * Defines the smallest and biggest atom that can be chosen for a certain
     * difficulty level. Easier levels limit themselves to more common atoms
     * while harder levels limit themselves to bigger and not so common atoms.
     */
    public enum ATOM_DIFFICULTY{
        EASY(1, 30),
        MEDIUM(30, 60),
        HARD(30, 92),
        GOD(50, 118);
        
        /**
         * Smallest atomic number an atom can have for that difficulty level.
         */
        private int minAtom;
        /**
         * Biggest atomic number an atom can have for that difficulty level.
         */
        private int maxAtom; 
        
        private ATOM_DIFFICULTY(int min, int max){
            minAtom = min;
            maxAtom = max;
        }
        
        public int getMinAtomicNumber(){return minAtom;}
        public int getMaxAtomicNumber(){return maxAtom;}
    }
    
    protected  ATOM_DIFFICULTY getDifficultyBasedOnScore(int score){
        if(score < 15) return ATOM_DIFFICULTY.EASY;
        else if(score > 15 && score < 30) return ATOM_DIFFICULTY.MEDIUM;
        else if (score > 30 && score < 50) return ATOM_DIFFICULTY.HARD;
        else return ATOM_DIFFICULTY.GOD;
    }

    /**
     * Generates 4 different Atoms and returns them in an array. These atoms
     * can be used to set the question for the round. The atoms are guaranteed
     * to be different.
     * @param dif
     * @return An array of Atom objects. Guaranteed to always have 4 items in it.
     * @throws SQLException The only way for the method to throw an excpetion
     * is by faiing to read the embedded database inside the .jar file. This
     * is rare udner normal conditions and will probably only happen if the
     * .jar is corrupted.
     * @throws java.lang.NoSuchFieldException
     */
    protected List<Atom> getRandomAtoms(ATOM_DIFFICULTY dif) throws SQLException, 
            NoSuchFieldException{
        int atomicNumbers[] = 
                new Random().ints(dif.getMinAtomicNumber(),
                        dif.getMaxAtomicNumber()).distinct().limit(4).toArray();
        
        List<Atom> retVal = new ArrayList<>();
        for(int x = 0; x < 4; x++){
            retVal.add(new Atom(atomicNumbers[x]));
        }
        return retVal;
    } 
    
    /**
     * Compares the answer passed trough the parameters with the correct
     * answer. Returns false if they are not equal. True if they are. 
     * @param <E> Type of answer passed to the parameters.
     * @param answer Variable that will be compared.
     * @return True if the parameter matches the answer and false if it doesnt.
     */
    protected <E> boolean verifyAnswer(E answer){
        return this.answer.equals(answer);
    }
    
    /**
     * Inner class that contains two methods and a string. In this context
     * the class holds a getter Method from the atom class and the String
     * contains a descrption of what the getter returns. This is can be used
     * to randomly generate different guesses for the user each round and 
     * display the string that displays what they are supposed to guess.
     */
    public class QuestionMap {
        
        private Method clueMeth;
        private Method questionMeth;
        private String descriptor;
        
        public QuestionMap(Method clMeth, Method meth, String str){
            this.clueMeth = clMeth;
            this.questionMeth = meth;
            this.descriptor = str;
        }
        
        /**
         * Returns the recommended method which returns data that can
         * be displayed to the user as a clue to aid him in guessing
         * the right answer.
         * @return getter Method from the Atom class. Can return a String or int
         */
        public Method getClueMethod(){
            return clueMeth;
        }
        
        /**
         * Returns the recommended method which returns data that can
         * be displayed to the user as a choice in the answer section.
         * @return getter Method from the Atom class. Can return a String or int
         */
        public Method getChoiceMethod(){
            return questionMeth;
        }
        
        /**
         * Returns a String which describes the information the user has to guess.
         * This information is the same as the information being given in the 
         * choices section.
         * @return String that described value returned by method returned
         *  by getChoiceMethod()
         */
        public String getDescriptor(){
            return descriptor;
        }
    }
    
    /**
     * Returns a list containing getter methods that returns variables which
     * can be guessed by the player. The objects inside the list
     * contain the getter method and a String which can be shown to the user
     * which explains what needs to be guessed.
     * @return List with QuestionMap objects, contains a getter method from the
     * Atom class and a String that details what the methods return.
     * @throws Exception If the methods can not be found.
     */
    public List<QuestionMap> getMethods() throws Exception{
        List<QuestionMap> retVal = new ArrayList<>();
        retVal.add(new QuestionMap(Atom.class.getDeclaredMethod("getSymbol"),
                Atom.class.getDeclaredMethod("getName"),
                LanguageLoader.getAppTranslation("nameLbl")));
        retVal.add(new QuestionMap(Atom.class.getDeclaredMethod("getName"),
                Atom.class.getDeclaredMethod("getSymbol"),
                LanguageLoader.getAppTranslation("symbolLbl")));
        retVal.add(new QuestionMap(Atom.class.getDeclaredMethod("getName"),
                Atom.class.getDeclaredMethod("getAtomicNumber"),
                LanguageLoader.getAppTranslation("atomNumLbl")));
        
        return retVal;
    }
}
