package domain;

import java.io.File;

import datasource.FileReader;

public class ClassReaderASM implements ClassReader {
    private org.objectweb.asm.ClassReader classReader;
    private ClassNode classNode;

    @Override
    public boolean acceptClass(String classPath) {
        try {
            this.classReader =
                    new org.objectweb.asm.ClassReader(new FileReader(new File(classPath)));
            this.classNode = new ClassNodeASM();
            this.classReader.accept(((ClassNodeASM) classNode).getAsmNode(),
                    org.objectweb.asm.ClassReader.EXPAND_FRAMES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ClassNode getClassNode() {
        return this.classNode;
    }
}
