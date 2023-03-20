import java.util.ArrayList;
import java.util.HashMap;

public class AttributesAndFD {
    private int sizeFD;
    private int sizeDecomp;
    private final Attributes attributes = new Attributes();
    private ArrayList<FunctionalDependency> functionalDependencies = new ArrayList<>();
    private ArrayList<Attributes> decomposition = new ArrayList<>();

    //Attributes:
    public boolean addAttributes(String input) {
        input = input.replaceAll(" ", "");
        char[] arr = input.toCharArray();

        for (char c : arr) {
            attributes.addAttributes(c);
        }
        return true;
    }

    //Functional Dependencies
    public void initializeFD(int size) {
        this.sizeFD = size;
        this.functionalDependencies = new ArrayList<>(size);
    }

    public int getSizeOfFD() {
        return this.sizeFD;
    }

    public boolean addFD(String str) {
        String[] FDs = str.split("->");

        //left => FDs[0] and right => FDs[1]

        FunctionalDependency curr = new FunctionalDependency(FDs[0], FDs[1]);
        if(curr.correctFD(this.attributes.getAttributes())) {
            this.functionalDependencies.add(curr);
            return true;
        } else {
            return false;
        }
    }

    //decomposition:
    public void initializeDecomposition(int size) {
        this.sizeDecomp = size;
        this.decomposition = new ArrayList<>(size);
    }

    public int getNumOfDecomposition() {
        return this.sizeDecomp;
    }

    public boolean addDecomposition(String input) {
        Attributes att = new Attributes();
        input = input.replaceAll(" ", "");
        char[] arr = input.toCharArray();

        for (char c : arr) {
            if(!attributes.contains(c)){
                return false;
            }

            att.addAttributes(c);
        }

        decomposition.add(att);
        return true;
    }

    public void printDecomposition() {
        System.out.println(Colors.ANSI_PURPLE + "Following is the decomposition:");
        for (Attributes att : decomposition) {
            System.out.println(att.getAttributes());
        }
    }

    //testing LossLess Decomposition
    public boolean testLossLessDecomposition() {
        /**
        * create an initial matrix with one row i for each relation Ri in Decomposition and
        * one column j for each attribute Aj in R*/
        String[][] matrix = new String[this.getNumOfDecomposition()][this.attributes.size()];

        /**
        * set matrix[i][j] = 'bij' for all matrix entries*/
        for(int i=0; i<matrix.length; i++) {
            for(int j=0; j<matrix[0].length; j++) {
                matrix[i][j] = "b" + i + j;
            }
        }
//        printMatrix(matrix);

        /**
         * For each row i representing relation schema Ri
         ****** For each column j representing attribute Aj
         ************* if (relation Ri include attribute Aj) => set matrix[i][j] = aj*/
        Object[] arr = this.attributes.toArray();

        HashMap<Character, Integer> mapOfCharToIndex = new HashMap<>();
        // a->1, f-2, example according to the HashSet
        for(int i=0; i<arr.length; i++) {
            mapOfCharToIndex.put((Character) arr[i], i);
        }

        for(int i=0; i<matrix.length; i++) {
            for(int j=0; j<matrix[0].length; j++) {
                // Decomposition Ri
                Attributes decompi = this.decomposition.get(i);

                // Attribute Aj
                char aj = (char) arr[j];

                if (decompi.contains(aj)) {
                    matrix[i][j] = "a" + j;
                }
            }
        }
//        printMatrix(matrix);

        /**
         * Repeat the following loop until a complete loop execution results in no changes to S
         * For each functional dependency X -> Y in F
         ****** For all rows in S which have the same symbols in the columns corresponding to attributes in X
         ************* Make the symbols in each column that correspond to an attribute in Y be the same in all these rows as follows:
         ********************* If any of the rows has an "a" symbol for the column, set the other rows to that
         ***************************** same "a" symbol in the column
         ********************* If no "a" symbol exists for the attribute in any of the rows, choose any of the
         ***************************** "b" symbols that appear in one of the rows for the attribute and set the other rows to
         ***************************** that same "b" symbol in the column

         * If a row is made up of entirely of "a" symbols, then the decomposition has the lossless join property, otherwise it does not.*/
        boolean flag = true;
        while(flag) {
            flag = false;
            boolean modified = false;
            for (FunctionalDependency fd : this.functionalDependencies) {
                ArrayList<Boolean> mark = new ArrayList<>();
                for(int j=0; j<this.sizeDecomp; j++) {
                    mark.add(true);
                }

                for(int i=0; i<this.sizeDecomp; i++) {
                    if(mark.get(i)) {
                        mark.set(i, false);
                        ArrayList<Integer> index = new ArrayList<>();

                        for(int j=0; j<this.sizeDecomp; j++) {
                            boolean isEqual = true;

                            char[] left = fd.getLeft().toCharArray();

                            for(int itrLeftAttr=0; itrLeftAttr<left.length; itrLeftAttr++) {
                                Character ch = left[itrLeftAttr];

                                if (!matrix[i][mapOfCharToIndex.get(ch)].equals(matrix[j][mapOfCharToIndex.get(ch)])) {
                                    isEqual = false;
                                }
                            }

                            if(isEqual) {
                                mark.set(j, false);
                                index.add(j);

                            }
                        }

                        char[] right = fd.getRight().toCharArray();
                        for(int itrRightAttr=0; itrRightAttr<right.length; itrRightAttr++) {
                            Character ch = right[itrRightAttr];
                            boolean isAnya = false;
                            for(Integer j: index) {
                                if(matrix[j][(int) mapOfCharToIndex.get(ch)].equals("a" + String.valueOf(mapOfCharToIndex.get(ch)))) {
                                    isAnya = true;
                                    for (Integer k: index) {
                                        if(!matrix[k][(int) mapOfCharToIndex.get(ch)].equals("a" + String.valueOf(mapOfCharToIndex.get(ch)))) {
                                            modified = true;
                                        }
                                        matrix[k][(int) mapOfCharToIndex.get(ch)] = "a" + String.valueOf(mapOfCharToIndex.get(ch));
                                    }
                                    break;
                                }
                            }
                            if(!isAnya) {
                                for(Integer k: index) {
                                    if (!matrix[k][(int) mapOfCharToIndex.get(ch)].equals(matrix[i][(int) mapOfCharToIndex.get(ch)])) {
                                        modified = true;
                                    }
                                    matrix[k][(int) mapOfCharToIndex.get(ch)] = matrix[i][(int) mapOfCharToIndex.get(ch)];
                                }
                            }
                        }
                    }
                }
            }
            if(modified) {
                flag = true;
            }
        }

        System.out.println("Final Matrix:");
        printMatrix(matrix);
        return checkingRowsAsA(matrix);
    }

    private boolean checkingRowsAsA(String[][] str) {
        for(String[] a: str) {
            boolean flag = true;
            for(String s: a) {
                if(s.charAt(0) == 'b') {
                    flag = false;
                }
            }

            if(flag) {
                return true;
            }
        }

        return false;
    }

    private void printMatrix(Object[][] matrix) {
        System.out.println("\n" + Colors.ANSI_BLUE);
        for (Object[] objects : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(objects[j] + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }


    @Override
    public String toString() {
        if(attributes.isEmpty()) {
            return "No attributes were added";
        } else if(functionalDependencies.isEmpty()) {
            return "Attributes are:\n" + attributes.getAttributes().toString();
        } else {
            StringBuilder stringBuilder = new StringBuilder("Attributes are:\n");
            stringBuilder.append(attributes.getAttributes().toString());

            stringBuilder.append("\n").append("Functional Dependencies are:\n");

            for(FunctionalDependency fd: functionalDependencies) {
                stringBuilder.append(fd.toString()).append("\n");
            }

            return stringBuilder.toString();
        }
    }
}
