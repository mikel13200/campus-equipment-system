//package edu.cit.dadula.michaelvincent.campusequipmentloan;
//
//import edu.cit.dadula.michaelvincent.campusequipmentloan.model.Equipment;
//import edu.cit.dadula.michaelvincent.campusequipmentloan.model.Student;
//import edu.cit.dadula.michaelvincent.campusequipmentloan.repository.EquipmentRepository;
//import edu.cit.dadula.michaelvincent.campusequipmentloan.repository.StudentRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DataLoader implements CommandLineRunner {
//    private final EquipmentRepository equipmentRepository;
//    private final StudentRepository studentRepository;
//
//    public DataLoader(EquipmentRepository equipmentRepository, StudentRepository studentRepository) {
//        this.equipmentRepository = equipmentRepository;
//        this.studentRepository = studentRepository;
//    }
//
//    @Override
//    public void run(String... args) {
//        Equipment eq1 = new Equipment();
//        eq1.setName("Laptop");
//        eq1.setType("Electronics");
//        eq1.setSerialNumber("SN001");
//        equipmentRepository.save(eq1);
//
//        Equipment eq2 = new Equipment();
//        eq2.setName("Projector");
//        eq2.setType("Electronics");
//        eq2.setSerialNumber("SN002");
//        equipmentRepository.save(eq2);
//
//        Student student = new Student();
//        student.setStudentNo("20231234");
//        student.setName("Juan Dela Cruz");
//        student.setEmail("juan@cit.edu");
//        studentRepository.save(student);
//    }
//}