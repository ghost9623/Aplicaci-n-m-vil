package com.monwareclinical.util;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.monwareclinical.R;
import com.monwareclinical.model.Clinic;
import com.monwareclinical.model.Medicine;
import com.monwareclinical.model.RouteOfAdministration;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final String URL_DEFAULT_PROFILE_PHOTO = "https://firebasestorage.googleapis.com/v0/b/monstersoft-8f986.appspot.com/o/default_photos%2Fblank_user.png?alt=media&token=53168e83-7011-4b19-926a-5fb5dba8cce1";

    static Constants instance;
    Context context;

    Clinic clinic;

    Constants(Context context) {
        this.context = context;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child(context.getString(R.string.fb_table_clinic));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clinic = snapshot.getValue(Clinic.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        createMedicines();
    }

    public static synchronized Constants getInstance(Context context) {
        if (instance == null)
            instance = new Constants(context);
        return instance;
    }

    public Clinic getClinic() {
        return clinic;
    }

    void createMedicines() {
        List<Medicine> medicineList = new ArrayList<>();

        medicineList.add(new Medicine(
                "",
                "Simvastatina",
                50d,
                "La simvastatina es un fármaco de la familia de las estatinas utilizado para disminuir los niveles de colesterol en sangre. Su importancia es grande, dada la trascendencia del colesterol como factor de riesgo cardiovascular.",
                RouteOfAdministration.ORAL,
                "Heces (60%) y orina (13%)",
                Medicine.MEDICINE_AVAILABLE));
        medicineList.add(new Medicine(
                "",
                "Ácido acetilsalicílico",
                50d,
                "El ácido acetilsalicílico, conocido popularmente como aspirina, nombre de una marca que pasó al uso común, es un fármaco de la familia de los salicilatos. Se utiliza como medicamento para tratar el dolor (analgésico), la fiebre (antipirético) y la inflamación (antiinflamatorio), debido a su efecto inhibitorio, no selectivo, de la ciclooxigenasa.",
                RouteOfAdministration.ORAL,
                "Renal (100%)",
                Medicine.MEDICINE_AVAILABLE));
        medicineList.add(new Medicine(
                "",
                "Ácido acetilsalicílico",
                50d,
                "El omeprazol se utiliza en el tratamiento de la dispepsia, úlcera péptica, enfermedades por reflujo gastroesofágico y el síndrome de Zollinger-Ellison.",
                RouteOfAdministration.ORAL + ", " + RouteOfAdministration.INJECTION,
                "Renal (80%) y Fecal (20%)",
                Medicine.MEDICINE_AVAILABLE));
        medicineList.add(new Medicine(
                "",
                "Levotiroxina",
                50d,
                "La levotiroxina, L-tiroxina, T4 sintética, o 3,5,3',5'-tetrayodo-L-tironina, es una forma sintética de la tiroxina (hormona tiroidea), usada como un reemplazo hormonal en pacientes con problemas de tiroides. La hormona natural se presenta químicamente bajo la forma quiral L, al igual que el agente farmacéutico.",
                RouteOfAdministration.ORAL + ", " + RouteOfAdministration.INJECTION,
                "Renal y Heces",
                Medicine.MEDICINE_AVAILABLE));
        medicineList.add(new Medicine(
                "",
                "Amlodipina",
                50d,
                "El amlodipino es un fármaco que se usa para tratar la hipertensión y la enfermedad de las arterias coronarias. Es un bloqueador de canales de calcio de efecto duradero que pertenece al grupo de las dihidropiridinas y trabaja parcialmente en la vasodilatación.",
                RouteOfAdministration.ORAL,
                "Renal",
                Medicine.MEDICINE_AVAILABLE));
        medicineList.add(new Medicine(
                "",
                "Paracetamol",
                50d,
                "El paracetamol, también conocido como acetaminofén o acetaminofeno, es un fármaco con propiedades analgésicas y antipiréticas utilizado principalmente para tratar la fiebre, y el dolor leve y moderado, aunque existen pocas evidencias de que su uso sea realmente eficaz en el alivio de la fiebre en niños.",
                RouteOfAdministration.ORAL + ", " + RouteOfAdministration.RECTAL + ", " + RouteOfAdministration.INJECTION,
                "Renal (85-90 %)",
                Medicine.MEDICINE_AVAILABLE));
        medicineList.add(new Medicine(
                "",
                "Atorvastatina",
                50d,
                "La atorvastatina es un fármaco de la familia de las estatinas utilizado para disminuir los niveles de colesterol en sangre y en la prevención de enfermedades cardiovasculares.",
                RouteOfAdministration.ORAL,
                "Bilis y Renal (2%)",
                Medicine.MEDICINE_AVAILABLE));
        medicineList.add(new Medicine(
                "",
                "Salbutamol",
                50d,
                "El salbutamol (DCI) o albuterol es un agonista β2 adrenérgico de efecto rápido utilizado para el alivio del broncoespasmo en padecimientos como el asma y la enfermedad pulmonar obstructiva crónica (EPOC).",
                RouteOfAdministration.ORAL + ", " + RouteOfAdministration.INJECTION + ", " + RouteOfAdministration.INHALATION,
                "Renal (69-90 %) y Heces (4%)",
                Medicine.MEDICINE_AVAILABLE));
        medicineList.add(new Medicine(
                "",
                "Furosemida",
                50d,
                "La furosemida es un diurético de asa utilizado en el tratamiento de la insuficiencia cardíaca congestiva, hipertensión y edemas. Junto con otros muchos diuréticos, la furosemida está incluida dentro de la lista de sustancias prohibidas de la Agencia Mundial Antidopaje, debido a que puede enmascarar la presencia de otras sustancias en el organismo.",
                RouteOfAdministration.ORAL + ", " + RouteOfAdministration.INJECTION,
                "Renal (66%) y Biliar (33%)",
                Medicine.MEDICINE_AVAILABLE));
        medicineList.add(new Medicine(
                "",
                "Lansoprazol",
                50d,
                "El lansoprazol es un medicamento del grupo de inhibidores de la bomba de protones que actúa a nivel del tracto gastrointestinal, reduciendo la secreción del ácido gástrico inhibiendo la ATPasa de la membrana celular de las células parietales del estómago.",
                RouteOfAdministration.ORAL + ", " + RouteOfAdministration.INJECTION,
                "Renal y Heces",
                Medicine.MEDICINE_AVAILABLE));
        medicineList.add(new Medicine(
                "",
                "Amoxicilina",
                50d,
                "La amoxicilina es un antibiótico semisintético derivado de la penicilina. Se trata de una amino penicilina. Actúa contra un amplio espectro de bacterias, tanto Gram positivos como Gram-negativos.",
                RouteOfAdministration.ORAL + ", " + RouteOfAdministration.INJECTION,
                "NA",
                Medicine.MEDICINE_AVAILABLE));
        medicineList.add(new Medicine(
                "",
                "Bendroflumetiazida",
                50d,
                "Bendroflumetiazida es el nombre del principio activo de un medicamento antihipertensivo del grupo de las tiazidas.",
                RouteOfAdministration.ORAL,
                "NA",
                Medicine.MEDICINE_AVAILABLE));
        medicineList.add(new Medicine(
                "",
                "Colecalciferol",
                50d,
                "El colecalciferol o vitamina D3 es una forma de vitamina D. Desempeña importantes funciones en la mineralización del hueso y el metabolismo del calcio.",
                "Rayos ultravioleta de la luz solar, a través de la ingesta de alimentos",
                "NA",
                Medicine.MEDICINE_UNAVAILABLE));

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.fb_table_clinic_medicines));
        for (Medicine m : medicineList) {
            myRef.child(m.getName()).setValue(m);
        }
    }
}