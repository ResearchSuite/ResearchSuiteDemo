package com.example.christina.researchsuitedemo.resultManagement;

import android.content.Context;
import android.support.annotation.Nullable;

import com.curiosityhealth.ls2sdk.omh.OMHAcquisitionProvenance;
import com.curiosityhealth.ls2sdk.omh.OMHDataPointBuilder;
import com.curiosityhealth.ls2sdk.omh.OMHSchema;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.cornell.tech.foundry.sdl_rsx_rsrpsupport.YADLSpotRaw;

/**
 * Created by christinatsangouri on 3/26/18.
 */

public class YADLSpotRawOMHDatapoint extends OMHDataPointBuilder {
    private YADLSpotRaw yadlSpotRaw;
    private OMHAcquisitionProvenance acquisitionProvenance;

    public YADLSpotRawOMHDatapoint(Context context, YADLSpotRaw yadlSpotRaw) {
        this.yadlSpotRaw = yadlSpotRaw;
        this.acquisitionProvenance = new OMHAcquisitionProvenance(
                context.getPackageName(),
                yadlSpotRaw.getStartDate(),
                OMHAcquisitionProvenance.OMHAcquisitionProvenanceModality.SELF_REPORTED
        );
    }

    @Override
    public String getDataPointID() {
        return this.yadlSpotRaw.getUuid().toString();
    }

    @Override
    public Date getCreationDateTime() {
        return this.yadlSpotRaw.getStartDate() != null ? this.yadlSpotRaw.getStartDate() : new Date();
    }

    @Override
    public OMHSchema getSchema() {

        Map<String, Object> schemaID = this.yadlSpotRaw.getSchemaID();

        if (schemaID.get("name") != null && (schemaID.get("name") instanceof String) &&
                schemaID.get("namespace") != null && (schemaID.get("namespace") instanceof String) &&
                schemaID.get("version") != null && (schemaID.get("version") instanceof String)
                ) {
            return new OMHSchema(
                    (String)schemaID.get("name") ,
                    (String)schemaID.get("namespace"),
                    (String)schemaID.get("version")
            );
        }

        return new OMHSchema(
                "yadl-spot-assessment",
                "Cornell",
                "2.1"
        );
    }

    @Nullable
    @Override
    public OMHAcquisitionProvenance getAcquisitionProvenance() {
        return this.acquisitionProvenance;
    }

    @Override
    public JSONObject getBody() {
        Map<String, Object> map = new HashMap<>();
        map.put("selected", this.yadlSpotRaw.getSelected());
        map.put("notSelected", this.yadlSpotRaw.getNotSelected());
        map.put("excluded", this.yadlSpotRaw.getExcluded());
        map.put("results", this.yadlSpotRaw.getResultMap());
        return new JSONObject(map);
    }
}