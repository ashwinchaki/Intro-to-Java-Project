
public class Test
{
    public static void main(String[] args)
    {
        int size = 7;
        for (int i = 0; i <= size/2; i++) {
            System.out.printf("%2d", size/2 + i);
            if (i > 0) {
                System.out.printf("%2d", size/2 - i);                
            }
        }
        System.out.println();
        size = 6;
        for (int i = 0; i < size/2; i++) {
            System.out.printf("%2d", size/2 + i);
            if (i > 0) {
                System.out.printf("%2d", size/2 - i);                
            }
        }

    
    }
}
