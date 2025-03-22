import os
import sys
import oracleData


def snake_to_pascal(snake_str):
    """Convert snake_case to PascalCase"""
    return ''.join(x.capitalize() for x in snake_str.lower().split('_'))

def create_directories(entity_name):
    base_path = "/home/slendy/JavaProjects/ecommerce/src/main/java/com/ecommerce/"
    
    # Convert to proper case and create paths
    entity_path = os.path.join(base_path, entity_name.lower())
    dto_path = os.path.join(entity_path, "dto")
    
    # Create directories
    os.makedirs(entity_path, exist_ok=True)
    print(f"Created entity directory: {entity_path}")
    
    os.makedirs(dto_path, exist_ok=True)
    print(f"Created DTO directory: {dto_path}")
    
    return {
        'entity_path': entity_path,
        'dto_path': dto_path,
        'package_name': f"com.ecommerce.{entity_name.lower()}"
    }

def generate_entity_class(paths, entity_class_name):
    """Generate entity class following our established patterns"""
    entity_file = os.path.join(paths['entity_path'], f"{entity_class_name}.java")
    columns = oracleData.return_structure(entity_class_name.lower())
    
    def get_java_type(oracle_type):
        type_mapping = {
            'VARCHAR2': 'String',
            'CHAR': 'String',
            'NUMBER': 'Long',
            'DATE': 'LocalDateTime',
            'TIMESTAMP': 'LocalDateTime',
            'BOOLEAN': 'Boolean'
        }
        return type_mapping.get(oracle_type, 'String')
    
    def to_camel_case(snake_str):
        components = snake_str.lower().split('_')
        return components[0] + ''.join(x.title() for x in components[1:])
    
    columns_content = "\n".join([
        f"    @Column(name = \"{column['column_name']}\", nullable = {str(column['nullable'] == 'Y').lower()})\n    private {get_java_type(column['data_type'])} {to_camel_case(column['column_name'].lower())};" 
        for column in columns
        if column['column_name'] != 'ID'
    ])
    
    # Following pattern from memory 64a8b2b3-0eae-48f6-a928-074536b9f18a
    entity_content = f'''package {paths['package_name']};

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.ecommerce.common.Audit;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "{entity_class_name.lower()}s")
public class {entity_class_name} {{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    {columns_content}

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Embedded
    private Audit audit;
}}
'''
    with open(entity_file, 'w') as f:
        f.write(entity_content)
    print(f"Created entity class: {entity_file}")

def generate_request_dto(paths, entity_class_name):
    """Generate request DTO following our established patterns"""
    request_file = os.path.join(paths['dto_path'], f"{entity_class_name}Request.java")
    columns = oracleData.return_structure(entity_class_name.lower())
    
    def get_java_type(oracle_type):
        type_mapping = {
            'VARCHAR2': 'String',
            'CHAR': 'String',
            'NUMBER': 'Long',
            'DATE': 'LocalDateTime',
            'TIMESTAMP': 'LocalDateTime',
            'BOOLEAN': 'Boolean'
        }
        return type_mapping.get(oracle_type, 'String')

    def to_camel_case(snake_str):
        components = snake_str.lower().split('_')
        return components[0] + ''.join(x.title() for x in components[1:])

    def get_validation_annotations(column):
        annotations = []
        field_name = to_camel_case(column['column_name'].lower())
        
        # Mapeo de nombres técnicos a nombres amigables
        friendly_names = {
            'provinceId': 'ID de la provincia',
            'code': 'código',
            'name': 'nombre',
            'description': 'descripción',
            'address': 'dirección',
            'phone': 'teléfono',
            'email': 'correo electrónico',
            'createdAt': 'fecha de creación',
            'updatedAt': 'fecha de actualización',
            'createdBy': 'creado por',
            'updatedBy': 'actualizado por'
        }
        
        friendly_name = friendly_names.get(field_name, field_name.replace('_', ' ').title())
        
        if column['nullable'] == 'N':
            if get_java_type(column['data_type']) == 'String':
                annotations.append(f'@NotBlank(message = "El {friendly_name} es obligatorio", groups = Create.class)')
            else:
                annotations.append(f'@NotNull(message = "El {friendly_name} es obligatorio", groups = Create.class)')
        
        if column['data_type'] in ['VARCHAR2', 'CHAR']:
            max_size = column['data_length']
            if column['data_type'] == 'CHAR':
                min_size = max_size
                annotations.append(f'@Size(min = {min_size}, max = {max_size}, message = "El {friendly_name} debe tener exactamente {min_size} caracteres")')
            else:
                annotations.append(f'@Size(max = {max_size}, message = "El {friendly_name} no puede tener más de {max_size} caracteres")')
        
        return annotations

    fields = []
    for column in columns:
        if column['column_name'] not in ['ID', 'IS_ACTIVE']:
            validations = get_validation_annotations(column)
            field_name = to_camel_case(column['column_name'].lower())
            field_type = get_java_type(column['data_type'])
            
            if validations:
                fields.extend([f"    {validation}" for validation in validations])
            fields.append(f"    private {field_type} {field_name};")
            fields.append("")  # Add blank line between fields
    
    fields_content = "\n".join(fields)
    
    # Following pattern from memory 9017e14e-06c6-4797-be3d-7600a0710504
    request_content = f'''package {paths['package_name']}.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class {entity_class_name}Request {{
    public interface Create {{}}

{fields_content}}}
'''
    with open(request_file, 'w') as f:
        f.write(request_content)
    print(f"Created request DTO: {request_file}")

def generate_response_dto(paths, entity_class_name):
    """Generate response DTO following our established patterns"""
    response_file = os.path.join(paths['dto_path'], f"{entity_class_name}Response.java")
    columns = oracleData.return_structure(entity_class_name.lower())
    
    def get_java_type(oracle_type):
        type_mapping = {
            'VARCHAR2': 'String',
            'CHAR': 'String',
            'NUMBER': 'Long',
            'DATE': 'LocalDateTime',
            'TIMESTAMP': 'LocalDateTime',
            'BOOLEAN': 'Boolean'
        }
        return type_mapping.get(oracle_type, 'String')
    
    def to_camel_case(snake_str):
        components = snake_str.lower().split('_')
        return components[0] + ''.join(x.title() for x in components[1:])
    
    fields = ["    private Long id;", "    private Boolean isActive;", "    private AuditResponse audit;"]
    fields.extend([
        f"    private {get_java_type(column['data_type'])} {to_camel_case(column['column_name'].lower())};"
        for column in columns
        if column['column_name'] not in ['ID', 'IS_ACTIVE']
    ])
    fields_content = "\n".join(fields)
    
    builder_lines = [
        "                .id(entity.getId())",
        "                .isActive(entity.getIsActive())",
        "                .audit(AuditResponse.fromAudit(entity.getAudit()))"
    ]
    builder_lines.extend([
        f"                .{to_camel_case(column['column_name'].lower())}(entity.get{to_camel_case(column['column_name']).title()}())"
        for column in columns
        if column['column_name'] not in ['ID', 'IS_ACTIVE']
    ])
    builder_content = "\n".join(builder_lines)
    
    response_content = f'''package {paths['package_name']}.dto;

import com.ecommerce.common.AuditResponse;
import {paths['package_name']}.{entity_class_name};
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class {entity_class_name}Response {{
{fields_content}

    public static {entity_class_name}Response fromEntity({entity_class_name} entity) {{
        return {entity_class_name}Response.builder()
{builder_content}
                .build();
    }}
}}
'''
    with open(response_file, 'w') as f:
        f.write(response_content)
    print(f"Created response DTO: {response_file}")

def generate_repository(paths, entity_class_name):
    """Generate repository following our established patterns"""
    repository_file = os.path.join(paths['entity_path'], f"{entity_class_name}Repository.java")
    
    # Following patterns from memories 131a52e6-9a70-49df-9218-82ff4461be7c and 8435f055-c1d4-4b18-af7e-9d08779ff3ea
    repository_content = f'''package {paths['package_name']};

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface {entity_class_name}Repository extends JpaRepository<{entity_class_name}, Long> {{
    @Override
    @NonNull
    @Query("SELECT e FROM {entity_class_name} e WHERE e.isActive = true ORDER BY e.id ASC")
    List<{entity_class_name}> findAll();

    @Query("SELECT e FROM {entity_class_name} e ORDER BY e.id ASC")
    List<{entity_class_name}> findAllIncludingInactive();

    @Query("SELECT e FROM {entity_class_name} e WHERE e.isActive = false ORDER BY e.id ASC")
    List<{entity_class_name}> findAllInactive();
}}
'''
    with open(repository_file, 'w') as f:
        f.write(repository_content)
    print(f"Created repository: {repository_file}")

def generate_controller(paths, entity_class_name):
    """Generate controller following our established patterns"""
    controller_file = os.path.join(paths['entity_path'], f"{entity_class_name}Controller.java")
    
    # Following pattern from memory 318a06d2-6037-4a61-9d47-17fcf706b33a
    controller_content = f'''package {paths['package_name']};

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import {paths['package_name']}.dto.{entity_class_name}Request;
import {paths['package_name']}.dto.{entity_class_name}Response;
import {paths['package_name']}.dto.{entity_class_name}Request.Create;

import java.util.List;

@RestController
@RequestMapping("/api/v1/{entity_class_name.lower()}s")
@RequiredArgsConstructor
public class {entity_class_name}Controller {{
    private final {entity_class_name}Service {entity_class_name.lower()}Service;

    @GetMapping
    public ResponseEntity<List<{entity_class_name}Response>> getAll{entity_class_name}s() {{
        return ResponseEntity.ok({entity_class_name.lower()}Service.getAll{entity_class_name}s());
    }}

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<{entity_class_name}Response>> getAll{entity_class_name}sIncludingInactive() {{
        return ResponseEntity.ok({entity_class_name.lower()}Service.getAll{entity_class_name}sIncludingInactive());
    }}

    @GetMapping("/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<{entity_class_name}Response>> getInactive{entity_class_name}s() {{
        return ResponseEntity.ok({entity_class_name.lower()}Service.getInactive{entity_class_name}s());
    }}

    @GetMapping("/{{id}}")
    public ResponseEntity<{entity_class_name}Response> get{entity_class_name}ById(@PathVariable Long id) {{
        return ResponseEntity.ok({entity_class_name.lower()}Service.get{entity_class_name}ById(id));
    }}

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<{entity_class_name}Response> create{entity_class_name}(
            @Validated(Create.class) @RequestBody {entity_class_name}Request request) {{
        return ResponseEntity.ok({entity_class_name.lower()}Service.create{entity_class_name}(request));
    }}

    @PutMapping("/{{id}}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<{entity_class_name}Response> update{entity_class_name}(
            @PathVariable Long id,
            @Valid @RequestBody {entity_class_name}Request request) {{
        return ResponseEntity.ok({entity_class_name.lower()}Service.update{entity_class_name}(id, request));
    }}

    @PostMapping("/{{id}}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<{entity_class_name}Response> deactivate{entity_class_name}(@PathVariable Long id) {{
        return ResponseEntity.ok({entity_class_name.lower()}Service.deactivate{entity_class_name}(id));
    }}

    @PostMapping("/{{id}}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<{entity_class_name}Response> reactivate{entity_class_name}(@PathVariable Long id) {{
        return ResponseEntity.ok({entity_class_name.lower()}Service.reactivate{entity_class_name}(id));
    }}
}}
'''
    with open(controller_file, 'w') as f:
        f.write(controller_content)
    print(f"Created controller: {controller_file}")

def generate_service(paths, entity_class_name):
    """Generate service following our established patterns"""
    service_file = os.path.join(paths['entity_path'], f"{entity_class_name}Service.java")
    
    service_content = f'''package {paths['package_name']};

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import {paths['package_name']}.dto.{entity_class_name}Request;
import {paths['package_name']}.dto.{entity_class_name}Response;
import com.ecommerce.common.exception.OracleException;
import com.ecommerce.common.Audit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class {entity_class_name}Service {{
    private final {entity_class_name}Repository {entity_class_name.lower()}Repository;

    public List<{entity_class_name}Response> getAll{entity_class_name}s() {{
        return {entity_class_name.lower()}Repository.findAll().stream()
                .map({entity_class_name}Response::fromEntity)
                .collect(Collectors.toList());
    }}

    public List<{entity_class_name}Response> getAll{entity_class_name}sIncludingInactive() {{
        return {entity_class_name.lower()}Repository.findAllIncludingInactive().stream()
                .map({entity_class_name}Response::fromEntity)
                .collect(Collectors.toList());
    }}

    public List<{entity_class_name}Response> getInactive{entity_class_name}s() {{
        return {entity_class_name.lower()}Repository.findAllInactive().stream()
                .map({entity_class_name}Response::fromEntity)
                .collect(Collectors.toList());
    }}

    public {entity_class_name}Response get{entity_class_name}ById(Long id) {{
        return {entity_class_name}Response.fromEntity(
            {entity_class_name.lower()}Repository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"))
        );
    }}

    public {entity_class_name}Response create{entity_class_name}({entity_class_name}Request request) {{
        var audit = Audit.builder()
                .createdBy("system")
                .createdAt(LocalDateTime.now())
                .updatedBy("system")
                .updatedAt(LocalDateTime.now())
                .build();

        var {entity_class_name.lower()} = {entity_class_name}.builder()
                .isActive(true)
                .audit(audit)
                .build();

        return {entity_class_name}Response.fromEntity({entity_class_name.lower()}Repository.save({entity_class_name.lower()}));
    }}

    public {entity_class_name}Response update{entity_class_name}(Long id, {entity_class_name}Request request) {{
        var {entity_class_name.lower()} = {entity_class_name.lower()}Repository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        {entity_class_name.lower()}.getAudit().setUpdatedBy("system");
        {entity_class_name.lower()}.getAudit().setUpdatedAt(LocalDateTime.now());

        return {entity_class_name}Response.fromEntity({entity_class_name.lower()}Repository.save({entity_class_name.lower()}));
    }}

    public {entity_class_name}Response deactivate{entity_class_name}(Long id) {{
        var {entity_class_name.lower()} = {entity_class_name.lower()}Repository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        {entity_class_name.lower()}.setIsActive(false);
        {entity_class_name.lower()}.getAudit().setUpdatedBy("system");
        {entity_class_name.lower()}.getAudit().setUpdatedAt(LocalDateTime.now());

        return {entity_class_name}Response.fromEntity({entity_class_name.lower()}Repository.save({entity_class_name.lower()}));
    }}

    public {entity_class_name}Response reactivate{entity_class_name}(Long id) {{
        var {entity_class_name.lower()} = {entity_class_name.lower()}Repository.findById(id)
                .orElseThrow(() -> new OracleException("ORA-01403", "No se han encontrado datos"));

        {entity_class_name.lower()}.setIsActive(true);
        {entity_class_name.lower()}.getAudit().setUpdatedBy("system");
        {entity_class_name.lower()}.getAudit().setUpdatedAt(LocalDateTime.now());

        return {entity_class_name}Response.fromEntity({entity_class_name.lower()}Repository.save({entity_class_name.lower()}));
    }}
}}
'''
    with open(service_file, 'w') as f:
        f.write(service_content)
    print(f"Created service: {service_file}")

def generate_entities(entity_name):
    # Convert to proper case for Java classes
    entity_class_name = snake_to_pascal(entity_name)
    
    # Create directory structure
    paths = create_directories(entity_name)
    
    # Generate files following our patterns
    generate_entity_class(paths, entity_name)
    generate_request_dto(paths, entity_class_name)
    generate_response_dto(paths, entity_class_name)
    generate_repository(paths, entity_class_name)
    generate_controller(paths, entity_class_name)
    generate_service(paths, entity_class_name)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python genEntities.py entity_name")
        print("Example: python genEntities.py municipality")
        sys.exit(1)
    generate_entities(sys.argv[1])