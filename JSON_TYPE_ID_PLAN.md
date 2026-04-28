# JsonTypeId Implementation Plan

## Goal
Add `@JsonTypeId` annotation to discriminator field in subtypes for explicit polymorphic typing.

## Current State
- ✅ Base schema (`Pet`) has `@JsonTypeInfo` + `@JsonSubTypes`
- ❌ Subtypes (`Cat`, `Dog`) do NOT have `@JsonTypeId` on discriminator field

## Desired Result
```java
// Cat.java
@JsonTypeId
private String petType;  // ← discriminator field marked with @JsonTypeId
```

## Implementation Steps

### Step 1: Pass discriminator name to subtypes
**File:** `SchemaMapper.java`

Find the discriminator field name from base schema and store in subtypes.

```java
// In processDiscriminators():
// For each subtype, find the discriminator field name from base schema
String discriminatorField = baseDiscriminator.get(baseName);  // e.g., "petType"
subtypeSchema.setDiscriminatorField(discriminatorField);
```

### Step 2: Add discriminator field to Schema
**File:** `Schema.java`

```java
private String discriminatorField;

public String getDiscriminatorField() { return discriminatorField; }
public void setDiscriminatorField(String field) { this.discriminatorField = field; }
```

### Step 3: Generate @JsonTypeId on field
**File:** `Schema.java` / `VariableProperties.java`

When generating fields, check if this field is the discriminator:

```java
// In field generation logic
if (schema.getDiscriminatorField() != null && 
    fieldName.equals(schema.getDiscriminatorField())) {
    fieldAnnotations.append("@JsonTypeId").append(lineSeparator());
    requiredImports.add("com.fasterxml.jackson.annotation.JsonTypeId;");
}
```

### Step 4: Add Dictionary constants
**File:** `Dictionary.java`

```java
public static final String JSON_TYPE_ID_ANNOTATION = "@JsonTypeId";
public static final String JSON_TYPE_ID_IMPORT = "com.fasterxml.jackson.annotation.JsonTypeId;";
```

## Notes

- `@JsonTypeId` is **optional** — Jackson works without it
- Use case: explicit typing when field name differs from type name
- Can be added later when needed

## References
- Jackson Docs: `@JsonTypeId`
- Test file: `discriminator.yaml`