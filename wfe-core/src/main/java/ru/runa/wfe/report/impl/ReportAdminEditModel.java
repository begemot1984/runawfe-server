package ru.runa.wfe.report.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ru.runa.wfe.report.ReportConfigurationType;
import ru.runa.wfe.report.ReportDefinition;
import ru.runa.wfe.report.ReportParameter;
import ru.runa.wfe.report.ReportParametersBuilder;

/**
 * Модель для редактирования параметров отчета при деплое.
 */
public class ReportAdminEditModel {

    private Long id;

    /**
     * Название отчета, отображаемое пользователю.
     */
    private String name;

    /**
     * Набор параметров, которые требуется запросить у пользователя для построения отчета.
     */
    private List<ReportAdminParameterEditModel> parameters;

    /**
     * Скомпилированный (.jasper) отчет jasper reports.
     */
    private byte[] compiledReport;

    /**
     * Тип конфигурации и построения отчета.
     */
    private ReportConfigurationType configType;

    /**
     * Содержимое JAR файла, требуемого для построения отчета. Используется для построения отчетов с типом конфигурации
     * {@link ReportConfigurationType.PARAMETER_BUILDER}.
     */
    private byte[] jarFile;

    /**
     * Название класса, реализующего интерфейс {@link ReportParametersBuilder} и используемого для заполнения параметров отчета. Используется для
     * построения отчетов с типом конфигурации {@link ReportConfigurationType.PARAMETER_BUILDER}.
     */
    private String parameterBuilderClassName;

    /**
     * Удаляет пустые записи о параметрах.
     */
    public void removeEntriesWithoutType() {
        Iterator<ReportAdminParameterEditModel> paramIter = parameters.iterator();
        while (paramIter.hasNext()) {
            ReportAdminParameterEditModel param = paramIter.next();
            if (param.getType() == null) {
                paramIter.remove();
            }
        }
    }

    public ReportAdminEditModel() {
        parameters = new ArrayList<ReportAdminParameterEditModel>();
    }

    public ReportAdminEditModel(ReportDefinition databaseDto, Map<String, String> paramNameToDescription) {
        id = databaseDto.getId();
        name = databaseDto.getName();
        parameters = new ArrayList<ReportAdminParameterEditModel>();
        if (databaseDto.getParameters() != null) {
            for (ReportParameter databaseParameterDto : databaseDto.getParameters()) {
                String description = paramNameToDescription.get(databaseParameterDto.getInnerName());
                parameters.add(new ReportAdminParameterEditModel(databaseParameterDto, description));
            }
        }
        compiledReport = databaseDto.getCompiledReport();
        jarFile = databaseDto.getJarFile();
        configType = databaseDto.getConfigType();
        parameterBuilderClassName = databaseDto.getParameterBuilderClassName();
    }

    /**
     * Обновляет DTO базы данных в соответствии с параметрами, полученными от пользователя.
     * 
     * @param databaseDto
     *            DTO базы данных, в которое копируются данные, введенные пользователем.
     */
    public void copyToDatabaseDto(ReportDefinition databaseDto) {
        databaseDto.setName(name);
        databaseDto.setConfigType(configType);
        databaseDto.setParameterBuilderClassName(parameterBuilderClassName);
        databaseDto.setCompiledReport(compiledReport);
        databaseDto.setJarFile(jarFile);
        if (databaseDto.getParameters() != null) {
            databaseDto.getParameters().clear();
        } else {
            databaseDto.setParameters(new ArrayList<ReportParameter>());
        }
        if (parameters != null) {
            for (ReportAdminParameterEditModel editedParameter : parameters) {
                databaseDto.getParameters().add(editedParameter.getDatabaseDto());
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ReportAdminParameterEditModel> getParameters() {
        return parameters;
    }

    public void setParameters(List<ReportAdminParameterEditModel> parameters) {
        this.parameters = parameters;
    }

    public String getParameterBuilderClassName() {
        return parameterBuilderClassName;
    }

    public void setParameterBuilderClassName(String parameterBuilderClassName) {
        this.parameterBuilderClassName = parameterBuilderClassName;
    }

    public byte[] getCompiledReport() {
        return compiledReport;
    }

    public void setCompiledReport(byte[] compiledReport) {
        this.compiledReport = compiledReport;
    }

    public ReportConfigurationType getConfigType() {
        return configType;
    }

    public void setConfigType(ReportConfigurationType configType) {
        this.configType = configType;
    }

    public byte[] getJarFile() {
        return jarFile;
    }

    public void setJarFile(byte[] jarFile) {
        this.jarFile = jarFile;
    }

    public boolean hasCompiledReport() {
        return compiledReport != null && compiledReport.length > 0;
    }

    public boolean hasJarFile() {
        return jarFile != null && jarFile.length > 0;
    }
}
